# swaggerTestDocuments
### 测试过程中使用过swagger跟RAP进行接口文档的整理。  
```如果是我自己看，自己记录，自己使用的话，没有那么复杂，我会把所有接口都用`soapUI`整理，这样做接口测试也方便，可偏偏是团队中每一个人都要把自己的接口整理出来，便于跟踪、调试以及公开相应的接口文档。这个时候我们就需要一款便于管理接口的平台工具，最初用的就是`swagger`，后来不知道什么情况，某一个领导就要大家们开始使用`RAP`啦！~我作为一个测试工程师，个人感觉swagger比RAP用起来方便，最起码swagger跟soapUI可以很好的结合起来。```  
### 在用swagger的过程中，也遇到过一些小问题，但在使用的过程中，总是在不断的学习，以便提高文档的质量以及编写文档的效率，我大致讲一下从一开始我是如何一步一步的走过来的  
1. 最开始测试接口就是用`postman`把用到的`request`一一保存，并最终整理到一个`collection`中，这样的好处是分享起来比较方便，`postman`帮助工程师把想要分享的`collection`上传到云上，其他人只要通过`share link`就可以获取到其他人编写好的所有接口请求（只是本地需要根据自身情况，创建相应的环境变量）.但是这样就有一个弊端啦，不管是谁都没有一份完整的文档把所有的接口都记录下来，即使有，一旦本地误操作也就完蛋啦~这个时候我们关注到swagger
2. **swagger**服务不是本人搭建的，具体如何搭建，官网应该有相应的说明文档，只需要知道`index.html`中指定的`URL`是哪个文件，这个文件是什么格式的（**swagger**支持`JSON`跟`YAML`两种格式）
3.接下来我主要关注如何更有效率的编辑我们接口的`JSON`文件   
4.在文档中我们需要配置多个参数，有些参数一旦设置啦，基本不会怎么变动了，例如swagger的`版本号`、`host`、`schemes`等。但有些参数内容我们要根据接口的变化来实时更新，例如：`paths`下的内容以及我们为其设置的`definitions`。  
5.先介绍一些不怎么变动的参数  
| Field Name     | Type        |                                                                                                                                                                     Description |  
| :------------- | :---------- | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |  
| swagger        | String      | **Required.** Specifies the Swagger Specification version being used. It can be used by the Swagger UI and other clients to interpret the API listing. The value MUST be "2.0". |  
| info           | Info Object | **Required.** Provides metadata about the API. The metadata can be used by the clients if needed.                                                                               |  
| host | String | The host (name or ip) serving the API. This MUST be the host only and does not include the scheme nor sub-paths. It MAY include a port. If the host is not included, the host serving the documentation is to be used (including the port). The host does not support path templating. |  
| basePath | String | The base path on which the API is served, which is relative to the host. If it is not included, the API is served directly under the host. The value MUST start with a leading slash (/). The basePath does not support path templating. |  
| schemes | [String] | The transfer protocol of the API. Values MUST be from the list: "http","https", "ws", "wss". If the schemes is not included, the default scheme to be used is the one used to access the Swagger definition itself. |  
| consumes | [string] | A list of MIME types the APIs can consume. This is global to all APIs but can be overridden on specific API calls. Value MUST be as described under Mime Types. |  
| produces | [string] | A list of MIME types the APIs can produce. This is global to all APIs but can be overridden on specific API calls. Value MUST be as described under Mime Types. |  
