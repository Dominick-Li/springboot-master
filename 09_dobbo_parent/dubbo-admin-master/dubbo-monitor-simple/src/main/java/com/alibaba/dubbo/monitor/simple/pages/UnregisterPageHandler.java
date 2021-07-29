/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.monitor.simple.pages;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.monitor.simple.common.Page;
import com.alibaba.dubbo.monitor.simple.container.RegistryContainer;
import com.alibaba.dubbo.monitor.simple.servlet.PageHandler;

/**
 * UnregisterPageHandler
 */
public class UnregisterPageHandler implements PageHandler {

    public Page handle(URL url) {
        String provider = url.getParameterAndDecoded("provider");
        if (provider == null || provider.length() == 0) {
            throw new IllegalArgumentException("Please input provider parameter.");
        }
        URL providerUrl = URL.valueOf(provider);
        RegistryContainer.getInstance().getRegistry().unregister(providerUrl);
        String parameter;
        if (url.hasParameter("service")) {
            parameter = "service=" + url.getParameter("service");
        } else if (url.hasParameter("host")) {
            parameter = "host=" + url.getParameter("host");
        } else if (url.hasParameter("application")) {
            parameter = "application=" + url.getParameter("application");
        } else {
            parameter = "service=" + providerUrl.getServiceInterface();
        }
        return new Page("<script type=\"text/javascript\">window.location.href=\"providers.html?" + parameter + "\";</script>");
    }

}
