# Reggie Custom

## 数据库建表

完成！

## 员工相关后台功能开发

### 登录逻辑

1. 前端post方式提交用户名，密码
2. 后端获得相应参数，从数据库中按按用户名查找用户
   1. 没有找到，提示用户没有权限
   2. 找到了，匹配密码是否正确
      1. 错误，提示密码错误
      2. 正确
         1. 判断员工状态是否禁用
            1. 禁用：返回已禁用
            2. 正常：获取用户登录态，存入session中
3. 登录成功

### 员工管理开发

1. 前端发送分页查询请求 - GET：/employee/page?page=1&pageSize=10

   分页查询固定步骤

   ```java
    @GetMapping("/page")
       public BaseResponse Page(int page, int pageSize, String name){
           log.info("page...");
           Page<Employee> pageInfo = new Page<>(page, pageSize);
           LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName, name);
           queryWrapper.orderByDesc(Employee::getUpdateTime);
           employeeService.page(pageInfo, queryWrapper);
           return ResultUtils.success(pageInfo);
       }
   ```

   **注意：**要想页面正常展示分页，还需要配置mybatis-plus的分页插件

   ```java
   @Configuration
   public class MybatisPlusConfig {
   
       @Bean
       public MybatisPlusInterceptor mybatisPlusInterceptor(){
           MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
           mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
           return mybatisPlusInterceptor;
       }
   }
   ```

   

2. 添加员工

   前端发送POST请求，并携带json格式数据

   ```json
   idNumber
   : 
   "120225446522152244"
   name
   : 
   "56546"
   phone
   : 
   "13325469954"
   sex
   : 
   "1"
   username
   : 
   "54533"
   ```

   注意：后端设计时，要给每个员工设置初始密码

   公共字段自动填充

3. 员工退出

   清除session
   
4. 对员工信息进行编辑

   1. 前端先发送一次请求，根据当前编辑员工的id查询当前员工信息用于回显

      **注意：**对于Long类型的id，前端会存在精度丢失问题，因此要把id转为字符串再传给前端 - GET

      在实体类对应字段上方，添加如下注解

      ```java
      @JsonFormat(shape = JsonFormat.Shape.STRING)
          private Long id;
      ```

      

   2. 修改完成后，点击保存，前端会再次发送update请求用于更新 - PUT


### 分类管理开发

1. 前端发送分页查询请求 - GET：/category/page?page=1&pageSize=10

