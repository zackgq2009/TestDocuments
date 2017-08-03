# swaggerTestDocuments
### 测试过程中使用过swagger跟RAP进行接口文档的整理。  
```如果是我自己看，自己记录，自己使用的话，没有那么复杂，我会把所有接口都用`soapUI`整理，这样做接口测试也方便，可偏偏是团队中每一个人都要把自己的接口整理出来，便于跟踪、调试以及公开相应的接口文档。这个时候我们就需要一款便于管理接口的平台工具，最初用的就是`swagger`，后来不知道什么情况，某一个领导就要大家们开始使用`RAP`啦！~我作为一个测试工程师，个人感觉swagger比RAP用起来方便，最起码swagger跟soapUI可以很好的结合起来。```  
### 在用swagger的过程中，也遇到过一些小问题，但在使用的过程中，总是在不断的学习，以便提高文档的质量以及编写文档的效率，我大致讲一下从一开始我是如何一步一步的走过来的

1. 最开始测试接口就是用`postman`把用到的`request`一一保存，并最终整理到一个`collection`中，这样的好处是分享起来比较方便，`postman`帮助工程师把想要分享的`collection`上传到云上，其他人只要通过`share link`就可以获取到其他人编写好的所有接口请求（只是本地需要根据自身情况，创建相应的环境变量）.但是这样就有一个弊端啦，不管是谁都没有一份完整的文档把所有的接口都记录下来，即使有，一旦本地误操作也就完蛋啦~这个时候我们关注到swagger

2. **swagger**服务不是本人搭建的，具体如何搭建，官网应该有相应的说明文档，只需要知道`index.html`中指定的`URL`是哪个文件，这个文件是什么格式的（**swagger**支持`JSON`跟`YAML`两种格式）

3.接下来我主要关注如何更有效率的编辑我们接口的`JSON`文件

4.在文档中我们需要配置多个参数，有些参数一旦设置啦，基本不会怎么变动了，例如swagger的`版本号`、`host`、`schemes`等。但有些参数内容我们要根据接口的变化来实时更新，例如：`paths`下的内容以及我们为其设置的`definitions`。

5.我们首先要搞明白，整个文档的'树'结构

```
"Swagger Object" : {
  "Paths Object": {
    "Operation Object": {
      "Parameter Object": {
        if('in' is 'body'), "Schema Object": {}
      },
      "Responses Object": {}
    }
  },
  "Definitions Object": {}
}
```

每一层`Object`下都有隶属于自己的参数，参数我们会在接下来的部分介绍。但不同级别的Object则需要一层一层嵌套。

6.先介绍一些不怎么变动的参数:

| Field Name | Type | Description |
| -----------| ---- | ----------- |
| swagger    | String | **Required.** Specifies the Swagger Specification version being used. It can be used by the Swagger UI and other clients to interpret the API listing. The value MUST be "2.0". |
| info       | Info Object | **Required.** Provides metadata about the API. The metadata can be used by the clients if needed. |
| host | String | The host (name or ip) serving the API. This MUST be the host only and does not include the scheme nor sub-paths. It MAY include a port. If the host is not included, the host serving the documentation is to be used (including the port). The host does not support path templating. |
| basePath | String | The base path on which the API is served, which is relative to the host. If it is not included, the API is served directly under the host. The value MUST start with a leading slash (/). The basePath does not support path templating. |
| schemes | [String] | The transfer protocol of the API. Values MUST be from the list: "http","https", "ws", "wss". If the schemes is not included, the default scheme to be used is the one used to access the Swagger definition itself. |
| consumes | [string] | A list of MIME types the APIs can consume. This is global to all APIs but can be overridden on specific API calls. Value MUST be as described under Mime Types. |
| produces | [string] | A list of MIME types the APIs can produce. This is global to all APIs but can be overridden on specific API calls. Value MUST be as described under Mime Types. |

这些参数一旦设置啦，基本不会变动，除非项目有大的升级，host跟basePath会随之有些许变化。

7.我们需要着重聊下`paths`以及`definitions`这两个参数

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| paths | Paths Object | **Required.** The available paths and operations for the API.|
| definitions | Definitions Object | An object to hold data types produced and consumed by operations.|

`paths`下主要是编辑我们接口的信息，其中包括接口的路径，接口的方法（get\post\put\delete\options\head\patch等)，以及该接口的某种方法下需要的参数、接口的响应内容。
`definitions`主要为`paths`下相关参数服务的，在`definitions`下可以定义某些变量，这些变量在`paths`下可以通过`$ref: '#/definitions/{parameterName}'`来调取的~（本人使用下来，觉得`definitions`下主要是定义一些会变化的`body`内容，因为在开发过程中，body的内容会时不时的增加一个字段、减少一个字段之类的，这个时候我们只需要关注`definitions`下的内容就行，无需更改`paths`下`schema`的内容）

8.那么`paths`下都有哪些参数呢？我们来一一列出来

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| $ref | String | Allows for an external definition of this path item. The referenced structure MUST be in the format of a Path Item Object. If there are conflicts between the referenced definition and this Path Item's definition, the behavior is undefined.|
| get | Operation Object | A definition of a GET operation on this path.|
| put | Operation Object | A definition of a PUT operation on this path.|
| post | Operation Object | A definition of a POST operation on this path.|
| delete | Operation Object | A definition of a DELETE operation on this path.|
| options | Operation Object | A definition of a OPTIONS operation on this path.|
| head | Operation Object | A definition of a HEAD operation on this path.|
| patch | Operation Object | A definition of a PATCH operation on this path.|
| parameters | [Parameter Object / Reference Object] | A list of parameters that are applicable for all the operations described under this path. These parameters can be overridden at the operation level, but cannot be removed there. The list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of a name and location. The list can use the Reference Object to link to parameters that are defined at the Swagger Object's parameters. There can be one "body" parameter at most.|

首先我们要知道同一个接口可能拥有多个方法，例如：/user接口就可能有post/get/put/delete四种基本操作~（新增、获取、更新以及删除四种操作~），我们在编写文档时要为同一个接口来编写多个`Operation Object`。

```
{
  "/user": {
    "post": {
      "description": "Add new user",
      "summary": "",
      "produces": [
        "application/json"
      ],
      "tags": [],
      "parameters": {

      }
      "responses": {
        "200": {
          "description": "A list of pets.",
          "schema": {
            "type": "array",
            "items": {
              "$ref": "#/definitions/pet"
            }
          }
        }
      }
    },
    "get": {

    },
    "put": {

    },
    "delete": {

    }
  }
}
```

9.在`Operation Object`下有几个参数是跟`Swagger Object`下相同的，例如：`consumes`、`produces`、`schema`、`security`等，当`Operation Object`下设置了相应参数值，他们会覆盖`Swagger Object`下设置好的全局值。`Operation Object`中的参数是我们最常用的，一些参数是我们需要根据项目的实际情况来一一设定的。

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| tags | [String] | A list of tags for API documentation control. Tags can be used for logical grouping of operations by resources or any other qualifier.(`tags`这个标签位相对比较重要，因为`swagger`中是通过`tags`来把众多接口信息归类的，一种`tags`就是一大类)|
| summary | String | A short summary of what the operation does. For maximum readability in the swagger-ui, this field SHOULD be less than 120 characters.|
| description | String | A verbose explanation of the operation behavior. GFM syntax can be used for rich text representation.|
| operationId | String | Unique string used to identify the operation. The id MUST be unique among all operations described in the API. Tools and libraries MAY use the operationId to uniquely identify an operation, therefore, it is recommended to follow common programming naming conventions.|
| parameters | [Parameter Object / Reference Object] | A list of parameters that are applicable for this operation. If a parameter is already defined at the Path Item, the new definition will override it, but can never remove it. The list MUST NOT include duplicated parameters. A unique parameter is defined by a combination of a name and location. The list can use the Reference Object to link to parameters that are defined at the Swagger Object's parameters. There can be one "body" parameter at most.|
| responses | Responses Object | **Required.** The list of possible responses as they are returned from executing this operation.|
| deprecated | boolean | Declares this operation to be deprecated. Usage of the declared operation should be refrained. Default value is false.|

实例：
```
{
  "tags": [
    "User"
  ],
  "summary": "Register new user",
  "description": "Register new user",
  "operationId": "addNewUser",
  "consumes": [
    "application/x-www-form-urlencoded"
  ],
  "produces": [
    "application/json",
    "application/xml"
  ],
  "parameters": [
    {
      "name": "appId",
      "in": "header",
      "description": "the unique Id of application",
      "required": true,
      "type": "string"
    },
    {
      "name": "UserBody",
      "in": "body",
      "description": "the information of the new user",
      "required": true,
      "type": "object",
      "schema": {
        "type": "object",
        "properties": {
          "": "",
          "": ""
        }
      }
    }
  ],
  "responses": {
    "200": {
      "description": "create new user successful"
    },
    "405": {
      "description": "Unexpected error",
      "schema" : {
        "$ref": "#/definitions/Error"
      }
    }
  },
  "security": [
    {
      "api_key": []
    }
  ]
}
```

10.紧随`Operation Object`的就是`Parameter Object`，operation下的参数主要是为了接口在swagger上的显示来设置的，而`Parameter Object`中的参数则是用户在使用接口时用来参考的。`Parameter Object`下常用的参数如下：

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| name | string | **Required.** The name of the parameter. Parameter names are case sensitive. If in is "path", the name field MUST correspond to the associated path segment from the path field in the Paths Object. See Path Templating for further information. For all other cases, the name corresponds to the parameter name used based on the in property.|
| in | string | **Required.** The location of the parameter. Possible values are "query", "header", "path", "formData" or "body".|
|description | string | A brief description of the parameter. This could contain examples of use. GFM syntax can be used for rich text representation.(`in`这个参数也很特殊，它的作用主要是标识出该参数是接口中哪部分的，是`query\header\path\formData\body`，一旦`in`设置错误了，对于查看接口文档的人来说就很麻烦啦，他们在调用相应接口时，传递参数就错误了，请求也就随之失败啦！)|
| required | boolean | Determines whether this parameter is mandatory. If the parameter is in "path", this property is required and its value MUST be true. Otherwise, the property MAY be included and its default value is false.|

If `in` is "body":如果in是body的时候，除上边四个参数外，只能设置schema这个参数啦

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| schema | Schema Object | **Required.** The schema defining the type used for the body parameter.|

If `in` is any value other than "body":如果in不是body的时候，还可以配置一下参数

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| type | string | **Required.** The type of the parameter. Since the parameter is not located at the request body, it is limited to simple types (that is, not an object). The value MUST be one of "string", "number", "integer", "boolean", "array" or "file". If type is "file", the consumes MUST be either "multipart/form-data", " application/x-www-form-urlencoded" or both and the parameter MUST be in "formData".(当`type`为`array`时，接下来的参数设置有有些特殊啦！)|
| format | string | The extending format for the previously mentioned type. See Data Type Formats for further details.|
| allowEmptyValue | boolean | Sets the ability to pass empty-valued parameters. This is valid only for either query or formData parameters and allows you to send a parameter with a name only or an empty value. Default value is false.|
| collectionFormat | string | Determines the format of the array if type array is used. Possible values are:csv - comma separated values foo,bar; ssv - space separated values foo bar; tsv - tab separated values foo\tbar; pipes - pipe separated values foo|bar; multi - corresponds to multiple parameter instances instead of multiple values for a single instance foo=bar&foo=baz. This is valid only for parameters in "query" or "formData"; Default value is csv.|
| default | * | Declares the value of the parameter that the server will use if none is provided, for example a "count" to control the number of results per page might default to 100 if not supplied by the client in the request. (Note: "default" has no meaning for required parameters.) See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-6.2. Unlike JSON Schema this value MUST conform to the defined type for this parameter.|
| maximum | number | See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.2. |
| minimum | number | See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.1.3. |
| enum | [*] | See https://tools.ietf.org/html/draft-fge-json-schema-validation-00#section-5.5.1. |

`Parameter Object`下还有其他一些参数，有一些参数不怎么常用，我也就不一一介绍啦，如果需要用到的话，请看这里：https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#parameterObject

11.我们的`Operation Object`下可以看到两种比较特殊的结构，一种是`in`是`body`类型时，其下需要设置`Schema Object`，另一种就是该`parameter object`的`type`是`array`时，它后边需要紧跟`items`这个参数，那么我这边就介绍下'Schema Object'跟`Items Object`

```
**Schema Object**

The Schema Object allows the definition of input and output data types. These types can be objects, but also primitives and arrays. This object is based on the JSON Schema Specification Draft 4 and uses a predefined subset of it. On top of this subset, there are extensions provided by this specification to allow for more complete documentation.
```

为了在swagger上凸显出接口传递的数据内容，我们一般都是通过`Schema Object`来定义其内容，我们可以在`'schema'`参数后直接编写其内容，也可以在`Definitions Object`中编写好内容，然后再调用。
![schema](https://github.com/zackgq2009/TestDocuments/blob/master/swaggerProjects/swaggerPictures/schemeObject.png)

`Schema Object`一般`type`都是`object`，所以在配置时，`"type": "object"`之后紧跟`"properties"`这个参数，然后在`properties`后再编写其内容。

`Schema Object`内容编写跟`Parameter Object`相类似，如果有什么疑问，可以查看这里：https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#schemaObject

然后，我们说一下`Items Object`，当用户定义一个`Parameter Object`的`type`是`array`时，后边就紧跟着`'items'`这个参数，在`items`参数内可以直接定义数组内的类型，可以是字符串，也可以是数字，当数组内是`object`的话，可以在items内编写`properties`。`properties`包含的内容就是数组中object的结构。

`Items Object`一般都是紧随`type`是`array`的，内容编写也跟`Parameter Object`相类似，如果有什么疑问，可以查看这里：https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#itemsObject

12.我们在设置接口中的参数时，一定要明确标注每一个参数的`数据类型`，因为`强类型语言`对数据类型有严格要求，那么swagger中是如何来规范`数据类型`的：

| Common Name | type | format | Comments |
| ----------- | ---- | ------ | -------- |
| integer | integer | int32 | signed 32 bits |
| long | integer | int64 | signed 64 bits |
| float | number | float | |
| double | number | double | |
| string | string | | |
| byte | string | byte | base64 encoded characters |
| binary | string | binary | any sequence of octets |
| boolean | boolean | | |
| date | string | date | As defined by full-date |
| dateTime | string | date-time | As defined by date-time |
| password | string | password | Used to hint UIs the input needs to be obscured |

13.`Responses Object`是描述接口的单个响应，在`Response Object`中包含以下类型的内容：

| Field Name | Type | Description |
| ---------- | ---- | ----------- |
| description | string | **Required.** A short description of the response. GFM syntax can be used for rich text representation. |
| schema | Schema Object | A definition of the response structure. It can be a primitive, an array or an object. If this field does not exist, it means no content is returned as part of the response. As an extension to the Schema Object, its root type value may also be "file". This SHOULD be accompanied by a relevant produces mime-type. |
| headers | Headers Object | A list of headers that are sent with the response. |
| examples | Example Object | An example of the response message. |

四个response的例子：

Response of an array of a complex type:

```
{
  "description": "A complex object array response",
  "schema": {
    "type": "array",
    "items": {
      "$ref": "#/definitions/VeryComplexType"
    }
  }
}
```

Response with a string type:

```
{
  "description": "A simple string response",
  "schema": {
    "type": "string"
  }
}
```

Response with headers:

```
{
  "description": "A simple string response",
  "schema": {
    "type": "string"
  },
  "headers": {
    "X-Rate-Limit-Limit": {
      "description": "The number of allowed requests in the current period",
      "type": "integer"
    },
    "X-Rate-Limit-Remaining": {
      "description": "The number of remaining requests in the current period",
      "type": "integer"
    },
    "X-Rate-Limit-Reset": {
      "description": "The number of seconds left in the current period",
      "type": "integer"
    }
  }
}
```

Response with no return value:

```
{
  "description": "object created"
}
```

14.`Definitions Object`

15.`Patterned Objects`
