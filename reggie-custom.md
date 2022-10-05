# Reggie Custom

## 数据库建表

完成！

## 后台管理功能开发

### 员工相关后台功能开发

#### 登录逻辑

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

#### 员工管理页面开发

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


### 分类管理页面开发

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

### 菜品管理页面开发

1. 新建菜品

   1. 点击该按钮后，前端发送请求 - GET /category/list?type=1，用于获取所有的菜品分类展示到下拉菜单中

      * 后端获取菜品分类信息，返回给前端

   2. 新增菜品时，需要上传菜品的图片，这时涉及到文件的上传和下载操作

      * 定义一个CommonController，用于管理上传和下载操作

      * 在配置文件中定义文件存储路径，并在controller中引用

        ```yaml
        reggie:
          path: F:\JAVA\项目\1 瑞吉外卖项目\upload\
        ```

        ```java
        @Value("${reggie.path}")
            private String basePath;
        ```

      * upload

        ```java
        @PostMapping("/upload")
            public BaseResponse upload(MultipartFile file){
                log.info(file.toString());
        
                //原始文件名
                String originalFilename = file.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
                String fileName= UUID.randomUUID().toString() + suffix;
        
                //创建一个目录对象，以便basePath不存在时进行创建
                File dir = new File(basePath);
                if (!dir.exists()){
                    //目录不存在，创建
                    dir.mkdirs();
                }
        
                try {
                    file.transferTo(new File(basePath + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ResultUtils.success(fileName);
            }
        ```

      * download

        ```java
        @GetMapping("/download")
            public void download(String name, HttpServletResponse response){
                try {
                    //输入流，通过输入流读取文件内容
                    FileInputStream fileInputStream = new FileInputStream(basePath + name);
                    //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
                    ServletOutputStream outputStream = response.getOutputStream();
        
                    response.setContentType("image/jpeg");
        
                    int len = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = fileInputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,len);
                        outputStream.flush();
                    }
        
                    outputStream.close();
                    fileInputStream.close();
        
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ```

   3. 点击保存，前端请求 - POST /dish

      1. 携带数据：菜品本身信息、口味信息
      2. 由于还携带了口味信息，单纯的dish实体类不能满足字段要求，使用DTO扩展字段
      3. 使用dto接收信息，向数据库中存储时，由于数据是两部分组成，因此要操作两张表

2. 菜品信息分页查询

   1. 前端发送分页查询请求 - GET /dish/page?page=1&pageSize=10

   2. 需要展示的字段

      菜品名称、图片、菜品分类、售价、售卖状态、最后操作时间、操作

   3. 后端开发

      * 简单的按照前面分页查询固定步骤来进行查询，发现**菜品分类**字段没有相应内返回

      * 这是由于dish实体类中没有菜品的分类名称，只有分类id，因此还要查询分类表

      * 具体实现：

        1. 创建分页构造器1，并查询分页信息，注意此时缺少**菜品分类**字段

           ```java
           Page<Dish> pageInfo = new Page<>(page, pageSize);
                   Page<DishDto> dishDtoPage = new Page<>();
           
                   LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
                   queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName, name);
                   queryWrapper.orderByDesc(Dish::getUpdateTime);
                   this.page(pageInfo, queryWrapper);
           ```

        2. 创建分页构造器2，并将创建分页构造器1的属性拷贝到2中，注意不要拷贝records属性

           ```java
           BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
           ```

        3. 针对records单独进行值的设置

           ```java
           List<Dish> records = pageInfo.getRecords();
                   List<DishDto> list = new ArrayList<>();
           
                   for (Dish item : records) {
                       DishDto dishDto = new DishDto();
                       BeanUtils.copyProperties(item, dishDto);
                       Long categoryId = item.getCategoryId();
                       Category category = categoryService.getById(categoryId);
                       if (category == null){
                           return ResultUtils.error(ErrorCode.NULL_ERROR);
                       }
                       String categoryName = category.getName();
                       dishDto.setCategoryName(categoryName);
                       list.add(dishDto);
                   }
           ```

        4. 将操作好的records送进创建分页构造器2中，并返回结果

           ```java
           dishDtoPage.setRecords(list);
           return ResultUtils.success(dishDtoPage);
           ```

3. 修改菜品信息

   1. 前端发送请求用于信息回显 -GET /dish/1577134630979608600
      * 应该返回带菜品口味信息的数据，也就是dishdto
   2. 修改后点击保存，前端发送请求 -PUT /dish
      * 和新增菜品类似，需要操作两张表
        * 携带数据：菜品本身信息、口味信息
        * 由于还携带了口味信息，单纯的dish实体类不能满足字段要求，使用DTO扩展字段
        * 使用dto接收信息，向数据库中存储时，由于数据是两部分组成，因此要操作两张表

4. 删除/批量删除菜品

   1. 前端发送请求 -DELETE /dish?ids=1577134630979608578，批量删除的话，ids是一个数组
   2. 后端开发

5. 启售、停售、批量启售、批量停售

   1. 前端发送请求 -POST /dish/status/0?ids=1569943520653893634
      * 0代表停售

### 套餐管理页面开发

1. 添加套餐

   1. 获取套餐菜品分类：前端发送请求 -GET dish/list?categoryId=1397844263642378242

   2. 点击保存：前端发送请求 -POST /setmeal

      1. 前端负载字段

         ```json
         {
           "name": "商务套餐A",
           "categoryId": "1413342269393674242",
           "price": 18500,
           "code": "",
           "image": "3cc781e0-c295-400a-8932-dce7f58d713c.jpg",
           "description": "",
           "dishList": [],
           "status": 1,
           "idType": "1413342269393674242",
           "setmealDishes": [
             {
               "copies": 1,
               "dishId": "1413342036832100354",
               "name": "北冰洋",
               "price": 500
             },
             {
               "copies": 1,
               "dishId": "1397849739276890114",
               "name": "辣子鸡",
               "price": 7800
             },
             {
               "copies": 1,
               "dishId": "1413385247889891330",
               "name": "米饭",
               "price": 200
             }
           ]
         }
         ```

      2. 现有实体类均不能满足要求，要扩展属性，使用DTO

         ```java
         public class SetmealDto extends Setmeal {
             
             private List<SetmealDish> setmealDishes;
         
             private String categoryName;
         }
         ```

      3. 用到了两张表：setmeal和setmealDish，因此保存信息也要分开保存

         **注意：向数据库插入数据时，看一下是否所有字段都有值再向里面插入，缺少的话要想办法获得值，并set之后，再插入**

2. 套餐信息分页查询

   1. 前端发送分页查询请求 - GET /setmeal/page?page=1&pageSize=10

      1. 需要展示的字段

         套餐名称、图片、套餐分类、售价、售卖状态、最后操作时间、操作

   2. 后端开发

      * 简单的按照前面分页查询固定步骤来进行查询，发现**套餐分类**字段没有相应内返回

      * 这是由于setmeal实体类中没有菜品的分类名称，只有分类id，因此还要查询分类表

      * 具体实现：

        1. 创建分页构造器1，并查询分页信息，注意此时缺少**菜品分类**字段

           ```java
           Page<Setmeal> pageInfo = new Page<>(page, pageSize);
                   Page<SetmealDto> setmealDtoPage = new Page<>();
           
                   LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
                   queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName, name);
                   queryWrapper.orderByDesc(Setmeal::getUpdateTime);
                   this.page(pageInfo, queryWrapper);
           ```

        2. 创建分页构造器2，并将创建分页构造器1的属性拷贝到2中，注意不要拷贝records属性

           ```java
           BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
           ```

        3. 针对records单独进行值的设置

           ```java
           List<Setmeal> records = pageInfo.getRecords();
                   List<SetmealDto> list = new ArrayList<>();
           
                   for (Setmeal item : records) {
                       SetmealDto setmealDto = new SetmealDto();
                       BeanUtils.copyProperties(item, setmealDto);
                       Long categoryId = item.getCategoryId();
                       Category category = categoryService.getById(categoryId);
                       if (category == null){
                           return ResultUtils.error(ErrorCode.NULL_ERROR);
                       }
                       String categoryName = category.getName();
                       setmealDto.setCategoryName(categoryName);
                       list.add(setmealDto);
                   }
           ```

        4. 将操作好的records送进创建分页构造器2中，并返回结果

           ```java
           setmealDtoPage.setRecords(list);
           return ResultUtils.success(setmealDtoPage);
           ```

3. 修改菜品信息

   1. 前端发送请求用于信息回显 -GET /setmeal/1577557552579792897
      * 应该返回带菜品信息的数据，也就是setmealdto

   2. 修改后点击保存，前端发送请求 -PUT /setmeal
      * 和新增套餐类似，需要操作两张表
        * 携带数据：套餐基本信息、套餐包含的菜品信息
        * 由于还携带了菜品信息，单纯的setmeal实体类不能满足字段要求，使用DTO扩展字段
        * 使用dto接收信息，向数据库中存储时，由于数据是两部分组成，因此要操作两张表

4. 删除/批量删除套餐

   1. 前端发送请求 -DELETE /setmeal?ids=1577557552579792897，批量删除的话，ids是一个数组

   2. 后端开发

5. 启售、停售、批量启售、批量停售
   1. 前端发送请求 -POST /setmeal/status/0?ids=1577555878767349762,1571840441052135426
      * 0代表停售

### 订单明细页面开发

1. 分页查询 -GET /order/page?page=1&pageSize=10

   需要展示的字段：订单号、订单状态、用户、手机号、地址、下单时间、实收金额、操作

   
