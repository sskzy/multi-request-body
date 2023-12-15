#### 多对象请求

- ##### @MultiRequestBody

  ```
  增加支持一次请求多个一般对象的装配
  ```

- ##### 使用注解

  ```java
  @RestController
  public class UserController {
  
      @PostMapping("/getPage")
      public void getPage(@MultiRequestBody Page page, @MultiRequestBody User user) {
          System.out.println(page);
          System.out.println(user);
      }
  }
  ```

  
