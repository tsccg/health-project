<html>
	<head>
		<meta charset="utf-8" />
		<title>Freemarker入门</title>
	</head>
	<body>
		<#--3.include指令-->
		<#include "head.ftl">
		<#--1.Freemarker注释-->
		${name} 你好，${message}
		<br/>
		
		<#--2.assign指令-->
		<#assign linkman = "范先生">
		联系人：${linkman}
		<br/>
		<#assign info={"phone":"123456789","address":"中国"}>
		电话：${info.phone}
		地址：${info.address}
		<br/>
		
		<#--4.if else指令-->
		<#assign success=true>
		<#if success == true>
		您通过了考核
		<#else >
		您未通过考核
		</#if>
		<br/>
		
		<#--5.list指令-->
		<#list foodList as food>
		名称：${food.name} 价格：${food.price}<br/>
		</#list>
	</body>
</html>