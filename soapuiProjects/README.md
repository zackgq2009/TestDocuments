# SoapUI 使用总结

>在写这篇总结之前，已经用了好久的soapui。写这篇文章的目的：一是想把自己长时间使用过程中发现的技巧以及使用方法更有条理的整理出来，二是帮助其他测试工程师更好的使用soapui这个工具。  
**以下内容都是基于soapui-5.3.0版本**

![indexPage](https://github.com/zackgq2009/TestDocuments/blob/master/soapuiProjects/readmepictures/indexPage.png)

-----------------------
-[SoapUI使用环境](#SoapUI使用环境)  
-[SoapUI使用版本](#SoapUI使用版本)  
-[SoapUI官方文档](#SoapUI官方文档)  
-[REST APIs](#REST-APIs)  
-[Test Suite](#Test-Suite)  
-[SoapUI Groovy Scripts](#SoapUI-Groovy-Scripts)  
-----------------------

## SoapUI使用环境

之前做手动测试，界面测试，功能测试甚至是兼容性测试根本用不到soapui、postman这些跟接口打交道的工具，后来跳槽到一家云服务公司，发现一个项目或者是产品的某一个阶段在提供完整功能之前，会给出N多个restful APIs，这些APIs中有一部分可能是给前端人员、或者客户端开发SDK用的，而另一部分则会直接投放到市场，供用户直接使用的（就好似大厂提供的那些接口，`http://weather.yahooapis.com/forecastrss`,`http://maps.googleapis.com`）。如果这个时候只是傻傻的等待某一个成品出来再进行测试，那么时间根本就来不及的。  
既然接口都出来了，测试人员就要及时介入进来，如果只是一个单独的接口，提供一个比较独立的服务时，我们可以直接使用postman进行测试，但当开发人员提供一系列接口，来实现一个比较复杂的服务时，postman就显得有点力不从心啦！这个时候我们就可以使用soapui来实现类似业务流操作的自动化测试！

## SoapUI使用版本

首先声明下，如果SoapUI NG Pro的价格可以控制在100美元以内，又或者我的老板为了产品质量的考虑愿意帮我买一个license的话，我是非常愿意用SoapUI NG Pro的。但是...我觉得SoapUI(OpenSource)其实还是比较好用的，除了在测试报告方面逊色于Pro之外，其他方面还是可以接受的。  
SoapUI-5.3.0是一个完善的稳定版本。[for MAC 下载地址](https://b537910400b7ceac4df0-22e92613740a7dd1247910134033c0d1.ssl.cf5.rackcdn.com/soapui/5.3.0/SoapUI-5.3.0.dmg)

## SoapUI官方文档

[官网文档](https://www.soapui.org/soapui-projects/soapui-projects.html)基本上为用户提供了一个较为完善的使用说明，照着文档上的步骤，我们可以很顺利的实现一个简单的测试流。而且本人在使用SoapUI中遇到什么问题也都是从官方文档中一步一步、慢慢的整理出解决方案的。

## SoapUI来实现Mock

在开发阶段经常遇到客户端的开发进度严重依赖服务端接口的实现，如果服务端由于各种原因推迟交付相应接口，那么客户端就要整体往后延期。测试阶段也会遇到由于某个服务存在异常，客户端的测试也会延期。这个时候我们就需要一个`Mock Server`来帮助我们继续下去。

## SoapUI项目结构

一个完整的soapui项目的结构：（其中MockService本人没有用过，所以相应的结构我是通过工具一步一步创建出来的，可能会有错误）  
- REST Project
  - REST Service
    - Resource
      - Method
        - Request
  - REST Suites
    - TestCase
      - TestStep
  - REST MockService
    - Mock Action
      - Mock Response

SOAPUI中各个层级模块之间的关系，以及层级的作用。

1. New REST Project（项目级别中包括：项目文件的保存目录，此项目中包括的Service，Suites，Mock）  
2. New REST Service（Service主要是区别Endpoints，例如：https://api.leap.as，或者https://cs.leap.as，并且Service中包括的Resources）  
3. New Resource（Resource主要是区别Path，例如：/2.0/classes/...或者/2.0/functions/…，并且可以增加Resource Parameters）  
4. New Method（Resource已经确定了Path，Method则在此Path的基础之上确定HTTP method：GET\POST\DELETE\PUT…，并且可以增加Method Parameters）  
5. New Request（Request中根据Method的方法，Endpoint则根据Service，Resource则根据Resource中的Path，在Request中可以设置Parameters、以及设置Resource Parameters、Method Parameters中的值，并且设置相应POST中的request内容）  
6. New TestSuite（通过字面名称可知，suite就是testCases的集合，并且suite可以选择该测试集合是按照并行或者串行来运行）  
7. New TestCase（**测试用例是我们需要研究的核心内容**，测试人员就是通过创建合适的测试用例来实现完整业务的接口测试）  
8. New TestStep（测试步骤是测试用例的组成部分，测试步骤的类型众多，例如：接口请求、参数设置、参数传递、脚本设置等）  
9. New Rest MockService（MockService主要分为SOAP MockService跟REST MockService，但由于工作中主要接触restful api，所以我以Rest MockService为例进行介绍）  
10. New Mock Action（每一个MockService下可以创建多个Mock Action，每一个Mock Action具有自己的HTTP Method跟HTTP Path）  
11. New Mock Response（在每一个Mock Action下用户又可以设置多个Mock Response，每一个Mock Response下都可以设置自己独特的HTTP Status Code、HTTP Header、Response Content。**而且还需要强调下，同一个Mock Action下的所有Mock Response是由触发逻辑控制的，可以‘sequence’或者‘script’进行触发的**）

当我们了解了soapui中各个模块的关系后，就可以根据自己的需求创建相关REST API的项目  
通常的做法就是我们先把要用的所有接口通过`Resource--Method--Request`创建出来，然后在这些模块的基础上，我们创建testsuite以及testcase，在testcase中我们可以根据`request`模块来创建具体的request teststeps，在steps中我们可以加入Property Transfer，Property Transfer中我们可以针对已有的step的参数、属性来传递之间的值，主要就是在response中取出特定的值，然后传下去。针对于不同格式的response，SOAPUI具有XPATH、XQuery、JSONPath等。**（本来我在使用selenium的时候，主要用xpath进行元素的查找，到了soapui发现JsonPath用起来更顺手）**  

## SoapUI中的参数

首先我们需要先知道可以在哪些地方配置参数，然后在哪些地方可以调用这些参数，最后再来说如何调用这些参数。  
1. 在`Project`层级上，我们就可以配置参数了。这层的参数，我们全局都可以调用。我们只需要在`Project`的`Overview`窗口中的`Properties`上添加、编辑、删除参数。然后我们在所有可以文本输入的地方都可以调用这些参数。调用方法：`${#Project#name}`  
2. 在`Service`一层上我们无法配置参数，或者换一种说法，这一层上我们配置的参数是一种比较特殊的参数，无需我们手动调用，在特定的模块中就会看得到的。这种参数就是`Endpoints`，我们在`Service`中可以配置多个`endpoint`，在配置这些endpoint时，也可以调用project中的参数，然后配置好的endpoint，在`request`以及`teststep`模块中都会看的到的。无需手动调用，只需要打开`endpoint`下拉菜单，选取任意配置好的`endpoint`。  
3. 在`Resource`层级上，我们可以配置`Resource parameters`，在这里配置的参数，其实也是无需手动调用的，因为当用户配置好`Resource Parameters`之后，这些参数会自动出现在`request`模块中。**需要说一下，`Resource Path`跟`Resource Parameter`的`Value`中都可以调用`Project`级别的参数，并且`Resource Parameters`的参数有一个比较特殊的属性，就是`Style`（下边我们会单独说一下style这个属性的）。**  
4. 在`Method`层级上，我们可以配置`Method Parameters`，这个参数其实跟`Resource parameters`相类似，只是作用域有所不同，正如名称一样，一个作用域是`Resource`级别的，另一个则是`Method`级别的。除作用域之外，其他属性都基本一致。  
5. 在`Request`层级上，我们可以配置任意参数。**需要说明一下，虽然在`request`中可以添加参数，但是这些参数有个属性就是`Level`，这个属性只有两种值，一种是`Resource`另一种是`Method`，也就是说明添加的参数其实作用域还是resource跟method层级的。** 对于`Request`模块来说，他已经是最底层的模块了，更常见的则是调用参数。`Request`模块中最大的特点就是可以去配置`body`的内容，在请求体中我们可以调用project中配置的参数值。调用方法还是：`${#Project#name}`  


## REST-APIs

## Test-Suite

## SoapUI-Groovy-Scripts
