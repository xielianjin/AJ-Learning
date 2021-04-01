> 前置了解知识
```bash
Thymeleaf、nginx配置、异步方法
```
> 效果
```bash
打开JD,点开相应商品，直接访问商品详情页,xxx.html
```
> 实战应用
```bash
电商项目中对商品详情页面的封装渲染。

封装spu信息、sku信息渲染

名词解释：
SPU = Standard Product Unit （标准产品单位）
SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
通俗点讲，属性值、特性相同的商品就可以称为一个SPU。

SKU=stock keeping unit(库存量单位)
SKU即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
SKU是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。在服装、鞋类商品中使用最多最普遍。

通俗讲就是产品跟商品。
```

> Nginx配置 （方案1）
```shell script
访问地址：http://uri/item.html

server {
    listen       80;
    server_name  www.xielianjin.com;

    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

    location /item {
        # 先找本地
        root html;
        if (!-f $request_filename) { #请求的文件不存在，就反向代理
            proxy_pass http://127.0.0.1:58081;
            break;
        }
    }

    location / {
        proxy_pass http://127.0.0.1:58081;
        proxy_connect_timeout 600;
        proxy_read_timeout 600;
    }
}
```
> openresty&lua 动态渲染 (方案2)
```
访问地址：http://uri/item?itemId=1

http块中

nginx.conf添加Lua扩展库
# 设置纯Lua扩展库的搜索路径(';;'是默认路径)
lua_package_path "/usr/local/openresty/lualib/?.lua;;";
# 设置C编写的Lua扩展模块的搜索路径(也可以用‘；；’)
lua_package_cpath "/usr/local/openresty/lualib/?.so;;"

include lua.conf;

==== lua.conf ====
#给nginx 分内存 128m 兆  缓存大小！用于存放热点数据（频繁使用的数据！）
lua_shared_dict my_cache 128m;

#nginx服务
server {
    listen  80;
    server_name _;

    #静态资源管理模板地址...
    set $template_location "/templates";
    set $template_root "/home/www/templates";
    
    # root /home/www/templates;      # "指定要导入的模板路径！"  

    # 访问目录去解析item.lua 去调用后端服务接口 渲染出静态页面
    location /item {
        default_type text/html;
        content_by_lua_file /home/www/templates/item.lua;
    }
}

=== item.lua ===
 -- 中文转码
ngx.header.content_type="text/html;charset=utf8"
# 拿nginx本地缓存
local itemCache = cache_ngx:get(itemCacheKey)
# 如果拿到去请求
if itemCache == "" or itemCache == nil then
local http = require("resty.http")
local httpc = http.new()
local resp,err = httpc:request_uri("http://127.0.0.1:58081",{
    method = "GET",
    path = "/item/"..itemId,
    #path = "/item/info?itemId="..itemId,
    keepalive = false
})
itemCache = resp.body
local expireTime = math.random(600,1200)
cache_ngx:set(itemCacheKey,itemCache,expireTime)
end

local cjson = require("cjson")
local itemCacheJSON = cjson.decode(itemCache)

local context = {
    itemId = itemCacheJSON.itemId,
}
local template = require("resty.template")
template.render("item.html", context)

=== item.html ===
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>{* itemId *}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
</head>
<body>
<p/>商品详情: {* itemId *}
</body>
</html>

```

> 进阶
```bash
微服架构
1. nginx中/item项应该指到静态页面服务器
2. 生成服务使用异步，并且为独立服务
3. 如果有多种类型，可以用AsyncTask做统一规划
```