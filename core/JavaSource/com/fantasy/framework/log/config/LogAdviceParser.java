package com.fantasy.framework.log.config;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.*;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogAdviceParser extends AbstractSingleBeanDefinitionParser {

	private static class Props {

		private String key, condition, method;
		private String[] caches = null;

		Props(Element root) {
			String defaultCache = root.getAttribute("cache");
			key = root.getAttribute("key");
			condition = root.getAttribute("condition");
			method = root.getAttribute(METHOD_ATTRIBUTE);

			if (StringUtils.hasText(defaultCache)) {
				caches = StringUtils.commaDelimitedListToStringArray(defaultCache.trim());
			}
		}

		<T extends CacheOperation> T merge(Element element, ReaderContext readerCtx, T op) {
			String cache = element.getAttribute("cache");
			String k = element.getAttribute("key");
			String c = element.getAttribute("condition");

			String[] localCaches = caches;
			String localKey = key, localCondition = condition;

			// sanity check
			if (StringUtils.hasText(cache)) {
				localCaches = StringUtils.commaDelimitedListToStringArray(cache.trim());
			} else {
				if (caches == null) {
					readerCtx.error("No cache specified specified for " + element.getNodeName(), element);
				}
			}

			if (StringUtils.hasText(k)) {
				localKey = k.trim();
			}

			if (StringUtils.hasText(c)) {
				localCondition = c.trim();
			}
			op.setCacheNames(localCaches);
			op.setKey(localKey);
			op.setCondition(localCondition);

			return op;
		}

		String merge(Element element, ReaderContext readerCtx) {
			String m = element.getAttribute(METHOD_ATTRIBUTE);

			if (StringUtils.hasText(m)) {
				return m.trim();
			}
			if (StringUtils.hasText(method)) {
				return method;
			}
			readerCtx.error("No method specified for " + element.getNodeName(), element);
			return null;
		}
	}

	private static final String CACHEABLE_ELEMENT = "cacheable";
	private static final String CACHE_EVICT_ELEMENT = "cache-evict";
	private static final String CACHE_PUT_ELEMENT = "cache-put";
	private static final String METHOD_ATTRIBUTE = "method";
	private static final String DEFS_ELEMENT = "caching";

	@Override
	protected Class<?> getBeanClass(Element element) {
		return CacheInterceptor.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		builder.addPropertyReference("cacheManager", LogNamespaceHandler.extractCacheManager(element));
		LogNamespaceHandler.parseKeyGenerator(element, builder.getBeanDefinition());

		List<Element> cacheDefs = DomUtils.getChildElementsByTagName(element, DEFS_ELEMENT);
		if (!cacheDefs.isEmpty()) {
			// Using attributes source.
			List<RootBeanDefinition> attributeSourceDefinitions = parseDefinitionsSources(cacheDefs, parserContext);
			builder.addPropertyValue("cacheOperationSources", attributeSourceDefinitions);
		} else {
			// Assume annotations source.
			builder.addPropertyValue("cacheOperationSources", new RootBeanDefinition(AnnotationCacheOperationSource.class));
		}
	}

	private List<RootBeanDefinition> parseDefinitionsSources(List<Element> definitions, ParserContext parserContext) {
		ManagedList<RootBeanDefinition> defs = new ManagedList<RootBeanDefinition>(definitions.size());

		// extract default param for the definition
		for (Element element : definitions) {
			defs.add(parseDefinitionSource(element, parserContext));
		}

		return defs;
	}

	private RootBeanDefinition parseDefinitionSource(Element definition, ParserContext parserContext) {
		Props prop = new Props(definition);
		// add cacheable first

		ManagedMap<TypedStringValue, Collection<CacheOperation>> cacheOpMap = new ManagedMap<TypedStringValue, Collection<CacheOperation>>();
		cacheOpMap.setSource(parserContext.extractSource(definition));

		List<Element> cacheableCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHEABLE_ELEMENT);

		for (Element opElement : cacheableCacheMethods) {
			String name = prop.merge(opElement, parserContext.getReaderContext());
			TypedStringValue nameHolder = new TypedStringValue(name);
			nameHolder.setSource(parserContext.extractSource(opElement));
			CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), new CacheableOperation());

			Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
			if (col == null) {
				col = new ArrayList<CacheOperation>(2);
				cacheOpMap.put(nameHolder, col);
			}
			col.add(op);
		}

		List<Element> evictCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHE_EVICT_ELEMENT);

		for (Element opElement : evictCacheMethods) {
			String name = prop.merge(opElement, parserContext.getReaderContext());
			TypedStringValue nameHolder = new TypedStringValue(name);
			nameHolder.setSource(parserContext.extractSource(opElement));
			CacheEvictOperation op = prop.merge(opElement, parserContext.getReaderContext(), new CacheEvictOperation());

			String wide = opElement.getAttribute("all-entries");
			if (StringUtils.hasText(wide)) {
				op.setCacheWide(Boolean.valueOf(wide.trim()));
			}

			String after = opElement.getAttribute("before-invocation");
			if (StringUtils.hasText(after)) {
				op.setBeforeInvocation(Boolean.valueOf(after.trim()));
			}

			Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
			if (col == null) {
				col = new ArrayList<CacheOperation>(2);
				cacheOpMap.put(nameHolder, col);
			}
			col.add(op);
		}

		List<Element> putCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHE_PUT_ELEMENT);

		for (Element opElement : putCacheMethods) {
			String name = prop.merge(opElement, parserContext.getReaderContext());
			TypedStringValue nameHolder = new TypedStringValue(name);
			nameHolder.setSource(parserContext.extractSource(opElement));
			CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), new CachePutOperation());

			Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
			if (col == null) {
				col = new ArrayList<CacheOperation>(2);
				cacheOpMap.put(nameHolder, col);
			}
			col.add(op);
		}

		RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchCacheOperationSource.class);
		attributeSourceDefinition.setSource(parserContext.extractSource(definition));
		attributeSourceDefinition.getPropertyValues().add("nameMap", cacheOpMap);
		return attributeSourceDefinition;
	}
}
