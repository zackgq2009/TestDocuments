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
11. New Mock Response（在每一个Mock Action下用户又可以设置多个Mock Response，每一个Mock Response下都可以设置自己独特的HTTP Status Code、HTTP Header、Response Content.**而且还需要强调下，同一个Mock Action下的所有Mock Response是由触发逻辑控制的，可以‘sequence’或者‘script’进行触发的**）


## REST-APIs

## Test-Suite

## SoapUI-Groovy-Scripts
