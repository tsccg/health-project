### 1.1 项目介绍

健康管理系统是一款应用于健康管理机构的业务系统，实现健康管理机构工作内容可视化、会员管理专业化、健康评估数字化、健康干预流程化、知识库集成化，从而提高健康管理师的工作效率，加强与会员间的互动，增强管理者对健康管理机构运营情况的了解。

### 1.2 技术架构

![](https://pic.imgdb.cn/item/61860e992ab3f51d9192aa59.png)

### 1.3 功能架构

![](https://pic.imgdb.cn/item/61860eac2ab3f51d9192c319.png)

### 1.4 软件开发流程

软件开发一般会经历如下几个阶段，整个过程是顺序展开，所以通常称为瀑布模型。

![](https://pic.imgdb.cn/item/61860ed62ab3f51d9192ff74.png)

### 1.5 项目结构

本项目采用maven分模块开发方式，即对整个项目拆分为几个maven工程，每个maven工程存放特定的一类代码，具体如下：

![](https://pic.imgdb.cn/item/61860eea2ab3f51d919315e5.png)

各模块职责定位：

health_parent：父工程，打包方式为pom，统一锁定依赖的版本，同时聚合其他子模块便于统一执行maven命令

health_common：通用模块，打包方式为jar，存放项目中使用到的一些工具类、实体类、返回结果和常量类

health_interface：打包方式为jar，存放服务接口

health_service_provider：Dubbo服务模块，打包方式为war，存放服务实现类、Dao接口、Mapper映射文件等，作为服务提供方，需要部署到tomcat运行

health_backend：传智健康管理后台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行

health_mobile：移动端前台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行