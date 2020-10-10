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
2. 在`Service`一层上我们无法配置参数，或者换一种说法，这一层上我们配置的是一种比较特殊的参数，无需我们手动调用，在特定的模块中就会看得到的。这种参数就是`Endpoints`，我们在`Service`中可以配置多个`endpoint`，在配置这些endpoint时，也可以调用project中的参数，然后配置好的endpoint，在`request`以及`teststep`模块中都会看的到的。无需手动调用，只需要打开`endpoint`下拉菜单，选取任意配置好的`endpoint`。  
3. 在`Resource`层级上，我们可以配置`Resource parameters`，在这里配置的参数，其实也是无需手动调用的，因为当用户配置好`Resource Parameters`之后，这些参数会自动出现在`request`模块中。**需要说一下，`Resource Path`跟`Resource Parameter`的`Value`中都可以调用`Project`级别的参数，并且`Resource Parameters`的参数有一个比较特殊的属性，就是`Style`（下边我们会单独说一下style这个属性的）。**  
4. 在`Method`层级上，我们可以配置`Method Parameters`，这个参数其实跟`Resource parameters`相类似，只是作用域有所不同，正如名称一样，一个作用域是`Resource`级别的，另一个则是`Method`级别的。除作用域之外，其他属性都基本一致。  
5. 在`Request`层级上，我们可以配置任意参数。**需要说明一下，虽然在`request`中可以添加参数，但是这些参数有个属性就是`Level`，这个属性只有两种值，一种是`Resource`另一种是`Method`，也就是说明添加的参数其实作用域还是resource跟method层级的。** 对于`Request`模块来说，他已经是最底层的模块了，更常见的则是调用参数。`Request`模块中最大的特点就是可以去配置`body`的内容，在请求体中我们可以调用project中配置的参数值。调用方法还是：`${#Project#name}`  
6. 在`testSuite`层级上，我们可以像在`Project`上一样配置任意参数，这些参数可以在此suite下的`testCase`以及`testSteps`中调用。调用方法是：`${#TestSuite#name}`。  
7. 在`testCase`层级上，配置参数的方法跟`testSuite`一样，除了这里参数的作用域小于`testSuite`之外，其他基本无差别。调用的方法是：`${#TestCase#name}`  
8. `testStep`的内容跟`request`的内容基本无差别。所以我们在这里就像配置`request`一样进行参数的配置。只是在`testStep`中可以调用`testSuite`以及`testCase`中的参数。  
9. `Properties TestStep`是一种比较特殊的`testStep`，所以在这边补充说明一下。在`Properties TestStep`中我们可以添加任意参数，并且这些参数可以调用`Project`、`TestSuite`以及`TestCase`中定义好的参数。但由于它隶属于`testStep`，所以`Properties TestStep`的作用域是“它以后的所有testStep”。  
10. 在SoapUI中，不管是哪个模块都可以调用本地系统环境变量，调用方法是：`${#System#name}`   

## SoapUI常用的变量
  1. ``uuid: ${=UUID.randomUUID()}``
  2. ``timeStamp: ${=System.currentTimeMillis()}``
  3. ``randomString: ${=org.apache.commons.lang.RandomStringUtils.random(24, charset.toCharArray())}``
  4. ``randomInteger: ${=random.randint(0,100)}  ||  ${=(int)(Math.random()*1000)}  ||  ${=Math.round(100*Math.random())}``
  5. ````
  6. ````
  7. ````
  8. ````
  9. ````

## SoapUI来实现Mock

在之前的工作中会遇到前端开发跟后端开发的进度不一致的情况，当后端先发布功能，我们可以通过接口进行测试，但当前端先发布了功能或者后端发布的功能存在严重缺陷的时候，我们就需要通过mock的手段来测试前端所发布的功能。这个时候我们就需要一个`Mock Server`来帮助我们继续下去。

## Assertion
  对于测试人员来说，断言才是我们最最关注的地方，因为在不同的环境，不同的时间，不同的功能版本中，入参可能会有变化，出参可能也有变化，但如何让机器帮我们判断这个接口是否正常，就需要对断言进行更准确的定义，例如：我们需要关注response会包含哪些字段，返回的字段中是否有长度的判断，如果想更灵活的进行断言，我们还可以借助数据库中的数据与response中的字段内容进行匹配。说了这么多，接下来我就把我常用的几种断言方法给大家们说一下。
  1. Valid HTTP Status Codes/Invalid HTTP Status Codes
      所有的请求都会有状态码，10x，20x，30x，40x，50x，当我们对于请求的接口有一定的了解后，可以提前判定返回的状态码是什么，我们就可以通过valid HTTP Status Codes来判断返回的结果是否正常。但是存在缓存，所以当反复请求同一个接口时，状态码也会有所变化，这个时候我们就需要保证它返回的状态不是40x\50x
  2. Contains/Not Contains
      对于Get、Post两个方法的请求，返回的结果中都包含一些字段内容，我们可以通过某一个或某几个字符来判断结果是否正常，例如：{result: true}/{result: false}，分别代表请求的结果是成功还是失败，我们可以通过true跟false两个字段来判断结果。
  3. JsonPath Count
      当返回的内容为Json String，我们可以通过JsonPath Count来判断返回的结果是否符合我们的要求，例如：{status:1; content{1:xx; 2:xx; 3:xx}}，我们可以通过统计$.content的长度是否符合我们的要求。
  4. JsonPath Match
      当返回的结果中，某一个字符的内容起到决定性作用，我们就可以通过JsonPath Match来判断请求是否正常，例如：{status:1; content{xxx}; result:success}, 我们可以通过$.result是否等于success来判断结果
  5. XPath Match
      XPath Match跟JsonPath Match的用法基本一致，都是通过判断某一个字节内容是否符合要求来断言(soapui中不经常使用xpath，所以xpath的语法还不是很熟悉)
  6. Script Assertion
  `// check for the amazon id header
    assert messageExchange.responseHeaders["x-amz-id-1"] != null
  // check that response time is less than 400 ms
    assert messageExchange.timeTaken < 400
  // check that we received 2 attachments
    assert messageExchange.responseAttachments.length == 2
  // check for RequestId element in response
    def holder = new XmlHolder( messageExchange.responseContentAsXml )
    assert holder["//ns1:RequestId"] != null`



## SoapUI-Groovy-Scripts
  我们在SoapUI中使用groovy script最多的场景是在assertion以及添加的groovy script teststep中，但在这两个场景中com.eviware.soapui包中的一些类并不是通用的，有些只能在groovy assertion中使用，有些只能在groovy script teststep中使用。接下来我详细说一下我遇到的这些特殊的类以及相应的用法
  1. `com.eviware.soapui.model.testsuite.TestRunner`，这个类只能在`groovy script`中使用，可以通过`testRunner.testCase`来返回一个`testCase`的对象，这个`testCase`对象就是`groovy script`所在的`testCase`，还可以通过`testRunner.testCase.testSuite`来返回一个`testSuite`对象，这个`testSuite`对象就是`groovy script`所在的`testSuite`，还可以通过`testRunner.testCase.testSuite.project`来返回一个`project`对象，这个`project`对象就是我们创建的项目对象，得到这些对象后我们就可以通过`.getPropertyValue("xxx")`来获取相应的属性, 通过`.setPropertyValue("xxx",yyy)`来设置相应的属性。
    ```
    def test = testRunner.testCase.project.getPropertyValue(“xxxxxx”)
    def test = testRunner.testCase.testSuite.getPropertyValue(“xxxxxx")
    def test = testRunner.testCase.getPropertyValue(“xxxxxx")
    def test = testRunner.testCase.testSteps[“xxxxxx”].getPropertyValue(“xxxxxx")
    ```
    **要想在script assertion中使用testRunner这个类，不能直接使用，如果直接使用会提示（[No such property: testRunner for class: Script1]）**

  2. `com.eviware.soapui.model.iface.MessageExchange`，这个类只能在`groovy assertion`中使用，通过`messageExchange.modelItem`可以返回一个对象，这个对象的作用与`testRunner`是一致的（我们在script assertion中只能直接使用messageExchange这个对象，所以我们可以通过def testCase = messageExchange.modelItem.testCase;获取到testCase这个对象，然后我们在testCase这个对象的基础上做相应的处理），接下来我们可以通过`.testCase.testSuite.project`来获取不同作用域范围的对象。而且可以使用`messageExchange.getResponseContent()`方法直接获取所在`testStep`的`response`返回内容。然后使用`new JsonUtil().parseTrimmedText(response)`把`response`解析成`json String`.

  3. `com.eviware.soapui.model.iface.SubmitContext`，此接口下拥有多个SubInterface，我们在soapui中可以直接使用context来获取当前testCase对象或其他对象，例如：
    ```
    context.getCurrentStep()
    context.getTestCase()
    context.getTestCase().getTestSuite()
    context.getTestCase().getTestSuite().getProject().getName()) //Get the name of the soapUI project
    context.getProperty(“测试步骤的名称”,"属性名称（例如：Request, Response, id，userId）")
    context.setProperty(“属性的名称”,”属性的值")
    ```
    而且在groovy scripts中我们可以直接使用def xxxxxx = context.expand(‘${#Project#XXXXXXX}')来获取某一个属性的值，使用这种方法是因为testCases中加入的Properties中的所有属性，可以直接通过${xxxxxxxxx}进行使用，这种方法可以在请求体中，以及很多断言中，但在groovy scripts以及groovy scripts断言中无法使用。

  4.我们在assert的时候，或者在脚本中尝尝会获取数据，为了最快速以及最准确的获取数据，我们从本地数据库读取，这个时候我们要通过groovy sql来获取，例如：
    ```
    import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange

    def host = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("host")
    def dbUrl = "jdbc:postgresql://" + host + ":port/dbName"
    def dbUser = "userName"
    def dbPassword = "password"
    def dbDriver   = "org.postgresql.Driver"

    def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

    def credential = sql.firstRow("SELECT * FROM tableName")

    def credentialId = new JsonUtil().parseTrimmedText(messageExchange.getResponseContent()).credential.id

    assert credentialId == credential.id
    ```
    groovy sql的使用方法跟JDBC基本思路是一致的

5. script中对response进行json格式化
    ``def slurperResponse = new JsonSlurper().parseText(Response)``

6. 我们在test case中可以对属性值进行判断并根绝判断结果来选择继续执行的test step，soapui中有两种方法实现此功能
    1. Conditional Goto testStep     
        ![conditionalGotoTestStep](https://github.com/zackgq2009/TestDocuments/blob/master/soapuiProjects/readmepictures/ConditionalGotoTestStep.png)
    2. Groovy Script
      ```
      My test case contains following steps
          1.Soap request (getAsset)
          2.Property transfer
          3.soap request
          4.Groovy script

          My Script is
            def aa =0
            while (aa < 3)
            {
              testRunner.gotoStepByName( "getAsset")
              aa++
            }
      ```     
      ![gotoStepByName](https://github.com/zackgq2009/TestDocuments/blob/master/soapuiProjects/readmepictures/gotoStepByName.png)

7. 我们在test case中需要重复调用某一个接口，我们可以使用递归的方式来实现
    1. DataSource & DataSource Loop testStep
      首先datasource可以读取数据库或者任意文件，返回一个数组类型的结果（可以视为一个数据的集合），我们可以遍历它返回回来的集合，一一读取它的值，而Datasource loop则是帮我们实现遍历的步骤，它会一遍一遍的读取datasource返回的集合中的数据，并且loop中会指定遍历数据之后重复执行哪一步
      ![DataSourceLoop](https://github.com/zackgq2009/TestDocuments/blob/master/soapuiProjects/readmepictures/DataSourceLoop.png)
    2. Groovy Script
      通过脚本更容易实现我们的递归，我们只需要gotoStepByName的时候选择递归的第一步骤，但需要注意在脚本中需要加入退出递归的条件判断

8. Assertion TestStep
    单独断言步骤中会根据我们选择的Source以及该source下某一个属性值来自动化匹配可以添加的assertions，所以我们需要第一步选择source（test case中的某一个测试步骤），然后再选择该步骤下的某一个属性值（request，response等），然后再选择可以为这个属性值添加的assertions     
    ![AssertionTestStep](https://github.com/zackgq2009/TestDocuments/blob/master/soapuiProjects/readmepictures/AssertionTestStep.png)


9. Assertion DB
    import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange

    def host = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("host")
    def dbUrl = "jdbc:postgresql://" + host + ":5432/phoenixdb"
    def dbUser = "phoenix"
    def dbPassword = "J23lMBSo5DQ!"
    def dbDriver   = "org.postgresql.Driver"

    def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

    def deviceId = messageExchange.modelItem.testCase.testSuite.getPropertyValue("Windows-DeviceId")

    def data = sql.rows("SELECT * FROM ph_device Where ph_device.id = " + deviceId)

    log.info(data.size())
    assert data.size == 0


10. Pattern in Assertion
    import java.util.regex.Matcher
    import java.util.regex.Pattern
    import java.net.URLEncoder
    import groovy.sql.Sql
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange

    def host = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("host")
    def dbUrl = "jdbc:postgresql://" + host + ":5432/phoenixdb"
    def dbUser = "phoenix"
    def dbPassword = "J23lMBSo5DQ!"
    def dbDriver   = "org.postgresql.Driver"

    def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

    def devices = sql.rows("Select ph_device.discover_method, ph_device.name, ph_device.version, ph_device.os_edition, ph_device.id From ph_device Where ph_device.access_ip = '${#Project#device1}'")

    //Select count(ph_device.id) as countDevice
    //From ph_device
    //Where ph_device.id In (Select ph_group_item.item_id From ph_group_item
    //  Where ph_group_item.group_id In (Select ph_group.id From ph_group
    //    Where ph_group.natural_id = 'PH_SYS_DEVICE_WINDOWS_SERVER'))

    def response = testRunner.testCase.testSteps["PostLogin"].getPropertyValue("Response")
    log.info(testRunner.testCase.getTestStepByName("PostLogin").testRequest.response.responseHeaders)
    def cookie

    Pattern pattern = Pattern.compile('id="javax.faces.ViewState">(.*)</update>')
    Matcher matcher = pattern.matcher(response)
    if(matcher.find()) {
        log.info(matcher.group(1))
        def responseCode = java.net.URLEncoder.encode(matcher.group(1), "UTF-8")
        testRunner.testCase.testSteps["Properties"].setPropertyValue("ViewState",responseCode)
    }
    testRunner.testCase.getTestStepByName("PostLogin").testRequest.response.responseHeaders.each { 
      if (it.key == "Set-Cookie"){
          cookie = it.value
          Pattern patternTwo = Pattern.compile("JSESSIONID=(.*?);")
          Matcher matcherTwo = patternTwo.matcher(cookie[0])
          log.info(cookie[0])
          if(matcherTwo.find()) {
            log.info("test information")
          testRunner.testCase.testSuite.project.setPropertyValue("cookie","JSESSIONID="+matcherTwo.group(1))
          }
        }
    }


11. testRunner.gotoStepByName()
    import com.eviware.soapui.model.testsuite.TestRunner
    import groovy.json.JsonSlurper

    def response = testRunner.testCase.testSteps["Get-Credential-Associations"].getPropertyValue("Response")
    //log.info(response)
    def jsonResponse = new JsonSlurper().parseText(response)
    log.info(jsonResponse.size())

    //def testCase = testRunner.testCase;
    //def testStepBack = testCase.testSteps["Get-Credentials"];
    //def testStepNext = testCase.testSteps["Logout"];

    if(jsonResponse.size() > 1){
    //	testStepBack.run(testRunner, context)
      testRunner.gotoStepByName("Get-Credential-Associations");
    } else {
    //	testStepNext.run(testRunner, context)
      testRunner.gotoStepByName("Get-Credentials");
    }


12. Update the content of RequestBody or Response
    import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange

    def reportRequestBody = context.expand('${#TestSuite#queryReportRequestBody}')
    def reportResponse = context.expand('${#TestSuite#queryReportResponse}')
    log.info(reportRequestBody)
    log.info(reportResponse)

    def responseJSON = new JsonUtil().parseTrimmedText(reportResponse)
    def requestJSON = new JsonUtil().parseTrimmedText(reportRequestBody)
    log.info(responseJSON.report)

    responseJSON.put("report", requestJSON)
    context.getTestCase().getTestSuite().setPropertyValue("reportResultRequestBody", responseJSON.toString())


13. Get random one data from DB
    Select *  From ph_event_attr_type
    ORDER BY RANDOM() 
    LIMIT 1


14.   import com.eviware.soapui.model.testsuite.TestRunner
      import com.eviware.soapui.support.JsonUtil
      import com.eviware.soapui.model.iface.MessageExchange

      def response = messageExchange.getResponseContent()
      log.info(response)
      def responseJSON = new JsonUtil().parseTrimmedText(response)
      log.info(responseJSON)

      def objectTypeName = responseJSON.headerData.objectTypeName
      log.info(objectTypeName)

      if(objectTypeName.toString() == '"status"' ){
          log.info(responseJSON.lightValueObjects[0].data[0])
          assert responseJSON.lightValueObjects[0].data[0].toString() == '"No report results found."'
        }
      else if(objectTypeName.toString() == '"results"'){
          log.info(messageExchange.modelItem.testCase.testSuite.getPropertyValue("reportCount"))
          assert responseJSON.lightValueObjects.size() == messageExchange.modelItem.testCase.testSuite.getPropertyValue("reportCount").toInteger()
        }

15.   import groovy.sql.Sql
      import com.eviware.soapui.model.testsuite.TestRunner
      import com.eviware.soapui.support.JsonUtil
      import com.eviware.soapui.model.iface.MessageExchange

      def host = testRunner.testCase.testSuite.project.getPropertyValue("host")
      def dbUrl = "jdbc:postgresql://" + host + ":5432/phoenixdb"
      def dbUser = "phoenix"
      def dbPassword = "J23lMBSo5DQ!"
      def dbDriver   = "org.postgresql.Driver"
      def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

      def parentId = testRunner.testCase.testSteps["GetGroup"].getPropertyValue("ph_group_parent_id")
      log.info(parentId)
      def path = testRunner.testCase.testSteps["GetGroup"].getPropertyValue("ph_group_display_name")
      //log.info(path)
      path = "/" + path;

      if(parentId == null){
      //		testRunner.cancel("Do not need to get Group")
          testRunner.gotoStepByName("Property Transfer 5")
        }
      else {
        log.info(path)
        while(parentId != null) {
            def parent = sql.firstRow("SELECT * FROM ph_group WHERE id = " + parentId)
      //			log.info(parent)
            parentId = parent.parent_id
            path = "/" + parent.display_name + path
            log.info(path)
          }
        testRunner.testCase.testSuite.setPropertyValue("groupPath", path)
        }


16. import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange

    //def response = messageExchange.getResponseContent()
    def userAgent = new JsonUtil().parseTrimmedText(messageExchange.getResponseContent()).userAgent.textValue()
    def dbUserAgent = messageExchange.modelItem.testCase.testSuite.getPropertyValue("userAgentUserAgent")
    log.info(userAgent)
    log.info(dbUserAgent)
    assert dbUserAgent.equals(userAgent)


    17.  import groovy.sql.Sql
          import com.eviware.soapui.model.testsuite.TestRunner
          import com.eviware.soapui.support.JsonUtil
          import com.eviware.soapui.model.iface.MessageExchange

          //def response = messageExchange.getResponseContent()
          def scriptContent = new JsonUtil().parseTrimmedText(messageExchange.getResponseContent()).scriptContent.textValue()
          def dbScriptContent = messageExchange.modelItem.testCase.testSuite.getPropertyValue("remediationScriptContent")
          log.info(scriptContent)
          log.info(dbScriptContent)
          //Pattern p = Pattern.compile('\\s*|\t|\r|\n')
          //Matcher m = p.matcher(scriptContent)
          //def test111 = m.replaceAll("")
          def updateScriptContent=scriptContent.replaceAll(" ", "").replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "")
          def updateDBScriptContent=dbScriptContent.replaceAll(" ", "").replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "")
          log.info(updateScriptContent)
          log.info(updateDBScriptContent)
          //log.info(test111)
          assert updateScriptContent.equals(updateDBScriptContent)

18. import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    import java.net.URLEncoder
    import com.ph.phoenix.commons.Utils
    import java.util.Arrays

    def widgets = messageExchange.modelItem.testCase.testSteps["GetWidgetsIdByDashboardGroup"].getPropertyValue("ph_dashboard_widget_order")
    log.info(widgets)
    def response = messageExchange.getResponseContent()
    log.info(response)

    if (widgets == null) {
    //	log.info("null")
      assert response.equals('[]')
      }
    else {
    //	log.info("not null")
      def widgetSize = widgets.split(',').size()
      log.info(widgetSize)
      def widgetOrder = new JsonUtil().parseTrimmedText(response)
      def widgetOrderString = widgetOrder.toString()
      def widgetOrderSize = widgetOrder.size()
      log.info(widgetOrderSize)
      assert widgetSize.equals(widgetOrderSize)
      for(def one in widgets.split(',')) {
          assert widgetOrderString.contains(one)
        }
      }

19. $.[?(@.name=="storage_type")].value[0]
    $['lightValueObjects'][?(@.data[1]=="${cloneReportName}")].objectId[0]

20. import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange
    import com.eviware.soapui.model.iface.SubmitContext

    def response = context.getProperty("Response")
    def responseJSON = new JsonUtil().parseTrimmedText(response)
    log.info(responseJSON)

    for(def item : responseJSON){
      def itemName = item.name.toString()
      if (itemName == '"storage_type"' ) {
          if(item.value.toString() != '"elastic"' ) {
              testRunner.testCase.testSuite.setPropertyValue("isES", "false")
              testRunner.cancel("it is not ES")
            }
          else if(item.value.toString() == '"elastic"' ) {
              testRunner.testCase.testSuite.setPropertyValue("isES", "true")
            }
        }
      else if (itemName == '"cluster_name"' ) {
          if(item.value != null) {
              testRunner.testCase.testSuite.setPropertyValue("EScluster_name", item.value.toString().replace('\"', ''))
            }
        }
      else if (itemName == '"cluster_ip"' ) {
          if(item.value != null) {
              testRunner.testCase.testSuite.setPropertyValue("EScluster_ip", item.value.toString().replace('\"', ''))
            }
        }
      else if (itemName == '"http_port"' ) {
          if(item.value != null) {
              def httpPort = item.value.toString().replace('\"', '')
              httpPort = httpPort.substring(0, httpPort.lastIndexOf("."))
              log.info(httpPort)
              testRunner.testCase.testSuite.setPropertyValue("EShttp_port", httpPort)
              
            }
        }
      else if (itemName == '"java_port"' ) {
          if(item.value != null) {
              def javaPort = item.value.toString().replace('\"', '')
              javaPort = javaPort.substring(0, javaPort.lastIndexOf("."))
              log.info(javaPort)
              testRunner.testCase.testSuite.setPropertyValue("ESjava_port", javaPort)
            }
        }
      }
    //log.info(context.testRunner)


21. import groovy.sql.Sql
    import com.eviware.soapui.model.testsuite.TestRunner
    import com.eviware.soapui.support.JsonUtil
    import com.eviware.soapui.model.iface.MessageExchange
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    import java.net.URLEncoder
    import com.ph.phoenix.commons.Utils
    import java.util.Arrays

    def host = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("host")
    def databaseDB = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("databaseDB")
    def databaseUsername = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("databaseUsername")
    def databasePassword = messageExchange.modelItem.testCase.testSuite.project.getPropertyValue("databasePassword")
    def dbUrl = "jdbc:postgresql://" + host + ":5432/"+databaseDB
    def dbUser = databaseUsername
    def dbPassword = databasePassword
    def dbDriver   = "org.postgresql.Driver"

    def sql = Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)

    def type = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'storage_type'")
    log.info(type)

    def response = context.getProperty("Response")
    //log.info(response)
    def responseJSON = new JsonUtil().parseTrimmedText(response)
    log.info(responseJSON)
    //log.info(type.value.toString())
    //check the storage type
    if (type.value.toString() == "nfs") {
      def mountPoint = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'mount_point'")
      def serverIP = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'server_ip'")
    //	log.info(mountPoint)
    //	log.info(serverIP)
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("mount_point", mountPoint.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("server_ip", serverIP.value.toString())

      for (def item : responseJSON) {
        def itemName = item.name.toString()
        def itemValue =  item.value.toString()
    //		log.info(itemName)
        if (itemName == '"mount_point"') {
          def dbValue = '"' + mountPoint.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"server_ip"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + serverIP.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        }
      }

    if (type.value.toString() == "elastic") {
      def clientType = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'client_type'")
      def dynamicShard = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'dynamic_shard'")
      def numberShards = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'number_of_shards'")
      def numberReplicas = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'number_of_replicas'")
      def userName = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'user_name'")
      def password = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'password'")
      def clusterName = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'cluster_name'")
      def clusterIP = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'cluster_ip'")
      def httpPort = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'http_port'")
      def javaPort = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'java_port'")
      def indexPerCustomer = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'index_per_customer'")
      def esVersion = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'es_version'")

      messageExchange.modelItem.testCase.testSuite.setPropertyValue("client_type", clientType.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("dynamic_shard", dynamicShard.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("number_of_shards", numberShards.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("number_of_replicas", numberReplicas.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("user_name", userName.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("password", password.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("cluster_name", clusterName.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("cluster_ip", clusterIP.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("http_port", httpPort.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("java_port", javaPort.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("index_per_customer", indexPerCustomer.value.toString())
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("es_version", esVersion.value.toString())

      for (def item : responseJSON) {
        def itemName = item.name.toString()
        def itemValue =  item.value.toString()
    //		log.info(itemName)
    //		log.info(itemName.getClass().getName())
        if (itemName == '"client_type"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + clientType.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"dynamic_shard"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + dynamicShard.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"number_of_shards"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + numberShards.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"number_of_replicas"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + numberReplicas.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"user_name"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + userName.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"password"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + password.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"cluster_name"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + clusterName.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"cluster_ip"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + clusterIP.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"http_port"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + httpPort.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"java_port"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + javaPort.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"index_per_customer"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + indexPerCustomer.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        if (itemName == '"es_version"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + esVersion.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        }
      }

    if (type.value.toString() == "local") {
      def diskName = sql.firstRow("Select ph_sys_conf.value From ph_sys_conf Where property = 'disk_name'")
      messageExchange.modelItem.testCase.testSuite.setPropertyValue("disk_name", diskName.value.toString())

        for (def item : responseJSON) {
        def itemName = item.name.toString()
        def itemValue =  item.value.toString()
    //		log.info(itemName)
        if (itemName == '"disk_name"') {
    //			log.info(item.value.toString())
          def dbValue = '"' + diskName.value.toString() + '"'
          assert dbValue.equals(itemValue)
          }
        }
      }

22. t${=System.currentTimeMillis()}
    ${hexString}
    ${#Project#cookie}; ${#Project#cookieS}