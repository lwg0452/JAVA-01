InboundFilter 过滤掉非 GET 方法请求,url 非 http://127.0.0.1/test 的请求,请求参数不含 net_gate 的请求.并且修改 Header
OutboundFilter 修改响应的 Header

调用示例:
```
http://localhost:8888/test?net_gate=11111  ==> gateway API 成功转发
http://localhost:8888/test 转发失败
http://localhost:8888/aaa/bbb 转发失败
http://localhost:8803  ==> backend service
```