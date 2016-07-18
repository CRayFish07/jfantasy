package org.jfantasy.member.rest.models.assembler;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.rest.CommentController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class CommentResourceAssembler extends ResourceAssemblerSupport<Comment, ResultResourceSupport> {

    public CommentResourceAssembler() {
        super(CommentController.class, ResultResourceSupport.class);
    }

    @Override
    protected ResultResourceSupport<Comment> instantiateResource(Comment entity) {
        return new ResultResourceSupport<>(entity);
    }

    @Override
    public ResultResourceSupport toResource(Comment entity) {
        ResultResourceSupport resource = createResourceWithId(entity.getId(), entity);
        return resource;
    }

    public Pager<ResultResourceSupport> toResources(Pager<Comment> pager) {
        Pager<ResultResourceSupport> _pager = new Pager<>(pager);
        _pager.setPageItems(this.toResources(pager.getPageItems()));
        return _pager;
    }
}