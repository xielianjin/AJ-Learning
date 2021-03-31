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

> Nginx配置
```shell script
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
> 进阶
```bash
微服架构
1. nginx中/item项应该指到静态页面服务器
2. 生成服务使用异步，并且为独立服务
3. 如果有多种类型，可以用AsyncTask做统一规划
```