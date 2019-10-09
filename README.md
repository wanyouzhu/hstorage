# HStorage

## 简介

这是一个用TDD（测试驱动开发）方式开发的文档存储框架，可用来将领域对象直接存储于PostgreSQL的JSON/JSONB字段上，支持非侵入式的映射。此仓库的目的是为了向那些想学习TDD技能的同学演示TDD在实际工作中的应用，欢迎拍砖交流！
<br>
本项目完全遵守TDD的手法，所有功能均由测试用例驱动产生，大致的开发过程可以通过查看完整的Git日志来还原。

## 脚本

* 生成 IDEA 工程

```shell
./gradlew idea
```

* 检查

```shell
./gradlew check
```

* 生成构建产物

```shell
./gradlew build
```


## 使用

请参考测试用例[StorageTest](/src/test/java/ltd/highsoft/hstorage/StorageTest.java)。
