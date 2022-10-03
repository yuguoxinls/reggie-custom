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

4. 功能完善——添加过滤器/拦截器

   对于未登录用户，要求其必须登录才能访问其他页面

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

   分页查询固定步骤

   ```java
   @GetMapping("/page")
       public BaseResponse Page(int page, int pageSize, String name){
           log.info("page...");
           Page<Category> pageInfo = new Page<>(page, pageSize);
           LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
           queryWrapper.like(StringUtils.isNotBlank(name),Category::getName, name);
           queryWrapper.orderByDesc(Category::getUpdateTime);
           categoryService.page(pageInfo, queryWrapper);
           return ResultUtils.success(pageInfo);
   ```

   **注意：**要想页面正常展示分页，还需要配置mybatis-plus的分页插件

2. 新增菜品分类- POST /category

3. 公共字段自动填充

   ```java
   @Slf4j
   @Component
   public class MyMetaObjectHandler implements MetaObjectHandler {
       /**
        * 插入操作自动填充
        * @param metaObject
        */
       @Override
       public void insertFill(MetaObject metaObject) {
           log.info("公共字段自动填充[insert]...");
           log.info(metaObject.toString());
           metaObject.setValue("createTime", LocalDateTime.now());
           metaObject.setValue("updateTime", LocalDateTime.now());
           metaObject.setValue("createUser", BaseContext.getCurrentId());
           metaObject.setValue("updateUser", BaseContext.getCurrentId());
       }
   
       /**
        * 更新操作自动填充
        * @param metaObject
        */
       @Override
       public void updateFill(MetaObject metaObject) {
           log.info("公共字段自动填充[update]...");
           log.info(metaObject.toString());
           metaObject.setValue("updateTime", LocalDateTime.now());
           metaObject.setValue("updateUser", BaseContext.getCurrentId());
       }
   }
   ```

   其中，BaseContext工具类用于获取当前用户id

   ```java
   /**
    * 基于ThreadLocal的工具类，用于保存和获取当前用户的id
    */
   public class BaseContext {
       private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
   
       //因为是工具类，所以都是public static
       public static void setCurrentId(Long id){
           threadLocal.set(id);
       }
   
       public static Long getCurrentId(){
           return threadLocal.get();
       }
   }
   ```

   **注意：**需要在启动类添加@ServletComponentScan注解，使其生效

4. 修改功能- PUT /category

5. 删除功能- DELETE /category?ids=1576818093819203585

   注意：不能简单根据id删除对应分类，要进一步判断该分类下是否关联了套餐和菜品

   1. 根据前端传过来的分类id，去套餐表和菜品表中查找是否有套餐和菜品与之关联
   2. 有关联：返回提示，告诉用户不能删除
   3. 无关联：返回提示，可以删除
