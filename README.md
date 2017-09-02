PageHelper 是 mybatis 的分页插件，使用 PageHelper 可以很方便的完成分页功能。
使用方式请参考官方文档。官方github地址：https://github.com/pagehelper/Mybatis-PageHelper

***

本人在使用spring boot 整合通用mapper时遇到了 Configuration 和 AutoConfiguration 的加载顺序的问题，最后确定是作者使用的加载方式不对，具体参考 https://github.com/fengcbo/mapper-spring-boot
顺便研究了一下 PageHelper 使用的starter发现也会有加载顺序的问题，所以讲 PageHelper 的starter也进行了重写，使用方式和原来方式相同。
