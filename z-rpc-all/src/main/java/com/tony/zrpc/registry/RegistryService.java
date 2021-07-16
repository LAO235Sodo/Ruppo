package com.tony.zrpc.registry;

import sun.reflect.generics.tree.VoidDescriptor;

import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Auther: ljh
 * @Date: 2021/7/14 22:06
 * @Description: 注册中心功能，注册服务，发现服务;
 *                          订阅服务;
 * @version: 1.0
 */
public interface RegistryService {
    public void register(URI uri);

    public void subscriber(String service,NotifyListener notifyListener) throws URISyntaxException;

    public void init(URI address);



}
