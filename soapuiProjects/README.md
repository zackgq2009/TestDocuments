# SoapUI 使用总结

>在写这篇总结之前，已经用了好久的soapui。写这篇文章的目的：一是想把自己长时间使用过程中发现的技巧以及使用方法更有条理的整理出来，二是帮助其他测试工程师更好的使用soapui这个工具。  
**以下内容都是基于soapui-5.3.0版本**

![indexPage](https://github.com/zackgq2009/soapuiTestDocuments/blob/master/readmepictures/indexPage.png)

-----------------------
-[SoapUI使用环境](#SoapUI使用环境)  
-[SoapUI使用版本](#SoapUI使用版本)  
-[SoapUI官方文档](#SoapUI官方文档)  
-[REST APIs](#REST APIs)  
-[Test Suite](#Test Suite)  
-[SoapUI Groovy Scripts](#SoapUI Groovy Scripts)  
-----------------------

## SoapUI使用环境

之前做手动测试，界面测试，功能测试甚至是兼容性测试根本用不到soapui、postman这些跟接口打交道的工具，后来跳槽到一家云服务公司，发现一个项目或者是产品的某一个阶段在提供完整功能之前，会给出N多个restful APIs，这些APIs中有一部分可能是给前端人员、或者客户端开发SDK用的，而另一部分则会直接投放到市场，供用户直接使用的（就好似大厂提供的那些接口，`http://weather.yahooapis.com/forecastrss`,`http://maps.googleapis.com`）。如果这个时候只是傻傻的等待某一个成品出来再进行测试，那么时间根本就来不及的。  
既然接口都出来了，测试人员就要及时介入进来，如果只是一个单独的接口，提供一个比较独立的服务时，我们可以直接使用postman进行测试，但当开发人员提供一系列接口，来实现一个比较复杂的服务时，postman就显得有点力不从心啦！这个时候我们就可以使用soapui来实现类似业务流操作的自动化测试！

## SoapUI使用版本

首先声明下，如果SoapUI NG Pro的价格可以控制在100美元以内，又或者我的老板为了产品质量的考虑愿意帮我买一个license的话，我是非常愿意用SoapUI NG Pro的。但是...我觉得SoapUI(OpenSource)其实还是比较好用的，除了在测试报告方面逊色于Pro之外，其他方面还是可以接受的。  
SoapUI-5.3.0是一个完善的稳定版本。[for MAC 下载地址](https://b537910400b7ceac4df0-22e92613740a7dd1247910134033c0d1.ssl.cf5.rackcdn.com/soapui/5.3.0/SoapUI-5.3.0.dmg)

## SoapUI官方文档

[官网文档](https://www.soapui.org/soapui-projects/soapui-projects.html)基本上为用户提供了一个较为完善的使用说明，照着文档上的步骤，我们可以很顺利的实现一个简单的测试流。而且本人在使用SoapUI中遇到什么问题也都是从官方文档中一步一步、慢慢的整理出解决方案的。

## REST APIs

## Test Suite

## SoapUI Groovy Scripts
