# 部署中心

### 描述

* 支持离线环境一键部署应用和微服务到指定机器上。

### 项目开发说明

* 本地前后端可以单独开发
* 后端开发：直接将dfas-deploy-center项目导入IDE（idea或eclipse），采用springboot方式一键启动。
* 前端开发：将src/main/frontend文件夹直接导入前端IDE（VS Code或WebStorm），先运行“npm install”，再启动“npm run dev”。

### 项目部署说明

* 1.IDE中打开dfas-deploy-center项目，打包，运行maven package；
* 2.拷贝target下jar包到指定目录，运行“java -jar dfas-deploy-center-0.0.1-SNAPSHOT.jar”。
* 3.浏览器输入： http://localhost:8080/deploy/index.html