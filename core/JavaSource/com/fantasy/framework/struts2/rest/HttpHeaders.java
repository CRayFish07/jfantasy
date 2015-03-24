/*
 * $Id: HttpHeaders.java 1026675 2010-10-23 20:19:47Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.fantasy.framework.struts2.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Type-safe rest-related informtion to apply to a response
 */
public interface HttpHeaders {

    /**
     * Applies the configured information to the response
     * @param request The request
     * @param response The response
     * @param target The target object, usually the action
     * @return The result code to process
     */
    String apply(HttpServletRequest request,
            HttpServletResponse response, Object target);
    
    /**
     * The HTTP status code
     */
    int getStatus();

    /**
     * The HTTP status code
     */
    void setStatus(int status);

    /**
     * The result code to process
     */
    String getResultCode();

}