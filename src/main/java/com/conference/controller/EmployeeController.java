package com.conference.controller;

import com.conference.entity.Employee;
import com.conference.utils.Status;
import com.conference.service.EmployeeService;
import com.conference.utils.JwtUtil;
import com.conference.utils.MD5Util;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**  登录功能实现
     * 登录需求
     * 地址: /employee/login
     * 方式: post
     * 参数:
     *    {
     *     "username":"zhangsan", //用户名
     *     "password":"123456"     //明文密码
     *    }
     * 返回:
     *   {
     *    "code":"0",         // 成功状态码
     *    "message":"success"   // 成功状态描述
     *    "data":{
     *         "token":"... ..." // 用户id的token
     *     }
     *  }
     *
     * 大概流程:
     *    1. 账号进行数据库查询 返回用户对象
     *    2. 对比用户密码(md5加密)
     *    3. 成功,根据adminId生成的token -> map key=token value=token值 - result封装
     *    4. 失败,判断账号还是密码错误,封装对应的枚举错误即可
     *
     * 正则表达式：^\S{5,16}$
     *      ^：表示匹配字符串的开头
     *      $：表示匹配字符串的的结尾
     *      \S：表示匹配非空白字符(即空格、制表符、换行符等)，且在引号内需要将\转义
     *      {5,16}：表示前面的字符（这里是\S）出现的次数范围是5到16次
     */
    @PostMapping("login")
    public Result login(@Pattern(regexp = "^\\S{2,16}$") String username,
                        @Pattern(regexp = "^\\S{5,16}$") String password){
        // 根据用户名查询用户
        Employee loginUser = employeeService.findByUsername(username);
        // 判断用户是否存在
        if (loginUser == null) return Result.error("用户不存在");
        // 存在则校验账号状态 - 正常使用才可登录
        if (!loginUser.getStatus().equals(Status.NORMAL_USING)) return Result.error("账号"+loginUser.getStatus());
        // 存在则校验用户密码
        if(MD5Util.encrypt(password+MD5Util.KEY).equals(loginUser.getPassword())){
            // 登录成功，获取token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getEmployeeId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            // 返回token
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    /**员工注册 - 添加员工
     * url地址：employee/regist
     * 请求方式：POST
     * 请求参数：
     * {
     *     "username":"员工1",
     *     "password":"123456",
     *     "name":"员工姓名1"
     *     "email":"1@qq.com"
     *     "phoneNumber":"12345678901"
     * }
     * 响应数据：
     * {
     *    "code":"0",
     *    "message":"success"
     *    "data":{}
     * }
     *
     * 实现步骤:
     *   1. 将密码加密
     *   2. 将数据插入
     *   3. 判断结果,成 返回0 失败 1
     */
    @PostMapping("regist")
    public Result regist(@RequestBody Employee employee){
        return employeeService.regist(employee);
    }

    /** 查询员工列表
     *  url地址：employee
     *  请求方式：GET
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":[
     *          employee1:{},
     *          employee2:{},
     *          ...
     *      ]
     *  }
     */
    @GetMapping
    public Result<List<Employee>> list(){
        Result result = employeeService.list();
        return result;
    }

    /** 根据编号查询员工信息
     *  url地址：employee/employeeDetailById
     *  请求方式：GET
     *  请求参数：
     *  {
     *      "employeeId":员工编号
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":{
     *          "username":"员工用户名",
     *          "name":"员工姓名",
     *          "email":"员工邮箱",
     *          "phoneNumber":"员工电话号码"
     *      }
     *  }
     */
    @GetMapping("employeeDetailById")
    public Result employeeDetailById(Integer employeeId){
        Employee employee = employeeService.findByEmployeeId(employeeId);
        return Result.success(employee);
    }

    /** 根据员工用户名查询员工信息
     *  url地址：employee/employeeDetailByUsername
     *  请求方式：GET
     *  请求参数：
     *  {
     *      "username":员工用户名
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":{
     *          "username":"员工用户名",
     *          "name":"员工姓名",
     *          "email":"员工邮箱",
     *          "phoneNumber":"员工电话号码"
     *      }
     *  }
     */
    @GetMapping("employeeDetailByUsername")
    public Result employeeDetailByUsername(String username){
        Employee employee = employeeService.findByUsername(username);
        return Result.success(employee);
    }

    /** 筛选各个状态的账号列表(方便管理员管理)
     *  url地址：employee/employeeDetailByUsername
     *  请求方式：GET
     *  请求参数：
     *  {
     *      "status": 员工账号当前状态
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data": [
     *          状态为status的员工账号列表
     *      ]
     */
    @GetMapping("employeeDetailByStatus")
    public Result<List<Employee>> employeeDetailByStatus(String status){
        return employeeService.findByStatus(status);
    }

     /** 修改员工基本信息
      *  url地址：employee
      *  请求方式：PUT
      *  发送请求前需要带上登录时返回的token
      *  修改信息分为两大类：
      *  1.员工修改：
      *     员工修改分为两类：
      *     (1)若员工未修改用户名：则直接更新信息
      *     (2)若员工修改了用户名：其他信息直接更新，但是用户名需要审核
      *         此处还设置防小人不防君子逻辑：
      *         考虑到员工可能会在已经修改用户名并进入审核阶段的基础上，将用户名修改回原来可以正常使用的用户名，
      *         所以当员工修改回原来的用户名之后，则会将账户状态重置为正常使用
      *  2.管理员修改：
      *     管理员一般只修改账户状态，其他信息一般由员工自行修改
      *     但后端给管理员开放上帝权限，若有需要可对员工信息直接进行修改，无需审核
      *  请求参数：
      *  {
      *      "employeeId":员工编号,
      *      "username":"员工用户名",
      *      "name":"员工姓名",
      *      "status":"账号状态(请前端设置为仅管理员可见)",
      *      "email":"员工邮箱",
      *      "phoneNumber":"员工电话号码"
      *  }
      *  响应数据：
      *  {
      *      "code":"0",
      *      "message":"success",
      *      "data":{}
      *  }
      */
    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee){

        // 保存要修改的账户
        Employee newEmployee = employeeService.findByEmployeeId(employee.getEmployeeId());
        if (newEmployee == null) return Result.error("用户不存在");

        // 获取当前登录客户的用户名，可能是管理员，也可能是员工
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        if (!username.equals("admin")){     // 若此时登录的账户是员工而不是管理员，则判断其是否修改用户名
            // 若传入的用户名与原来的用户名相同
            if(employee.getUsername().equals(username)){
                // 防小人不防君子逻辑
                // 若checkUsername字段不为空，则表示员工在管理员审核用户名前已经修改过用户名，但是又重新改回原来可以正常使用的用户名
                if(newEmployee.getCheckUsername() != null) {
                    employee.setStatus(Status.NORMAL_USING);    // 状态重置为正常使用
                    employee.setCheckUsername(null);    // 将checkUsername字段重置为null
                }
                employeeService.updateEmployee(employee); // 更新员工信息
                return Result.success("信息已更新");
            }
            // 若传入的用户名与原来的用户名不同则表示员工修改了用户名
            employee.setCheckUsername(employee.getUsername());  // 将修改的username字段保存到checkUsername字段中
            employee.setStatus(Status.CHECKING);    // 状态置为审核中
            employee.setUsername(username); // 同时保持原来的username
            employeeService.updateEmployee(employee);   // 更新员工账户的其他信息
            return Result.success("用户名修改成功，等待管理员审核");
        }
        // 若此时登录的账户是管理员
        // 检查员工是否修改过用户名，若修改过则checkUsername应该不为空
        if(!employee.getStatus().equals(Status.NORMAL_USING)){  // 若审核不通过
            employee.setCheckUsername(null);    // 将checkUsername字段重置为null，表示该用户名审核不通过
        }else{     // 若审核通过，则将审核通过的checkUsername赋给username字段，同时重置checkUsername字段为null
            employee.setUsername(newEmployee.getCheckUsername());  // 将checkUsername字段的值赋给username字段
            employee.setCheckUsername(null);    // 前端admin传入的数据还是会有checkUsername，所以重置checkUsername字段的值
        }
        // 情况1：员工修改了用户名，在对上述的checkUsername字段进行操作后更新员工信息
        // 情况2：若员工没有修改用户名，但是管理员修改了用户名，则上述的checkUsername字段为空，无需审核，直接更新信息
        employeeService.updateEmployee(employee); // 更新员工信息
        return Result.success("信息已更新");
    }

    /** 根据id删除员工
     *  url地址：employee
     *  请求方式：DELETE
     *  请求参数：
     *  {
     *      "employeeId":员工编号
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success",
     *      "data":{}
     *  }
     */
    @DeleteMapping
    public Result deleteEmployee(Integer employeeId){
        employeeService.deleteEmployee(employeeId);
        return Result.success();
    }

    /** 更新密码 - 需二次确认
     *  url地址：admin
     *  请求方式：PATCH
     *  请求参数：
     *  {
     *      "old_pwd":"原密码",
     *      "new_pwd":"新密码",
     *      "re_pwd":"确认密码"
     *  }
     *  响应数据：
     *  {
     *      "code":"0",
     *      "message":"success"
     *      "data":{}
     *  }
     */
    @PatchMapping
    public Result updatePassword(@RequestBody Map<String, String> params){
        // 1.校验参数
        String oldPwd = params.get("old_pwd");  // 旧密码
        String newPwd = params.get("new_pwd");  // 新密码
        String rePwd = params.get("re_pwd");    // 确认密码

        // (1)判断是否为空
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        // (2)判断原密码是否正确
        // 根据用户名获取原密码，再和输入的old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String username = (String) map.get("username");
        Employee loginUser = employeeService.findByUsername(username);
        // 获取的密码是加密后的，需要将旧密码先加密再比较
        if(!loginUser.getPassword().equals(MD5Util.encrypt(oldPwd+MD5Util.KEY))) return Result.error("原密码错误，请重新输入");

        // (3)判断new_pwd和old_pwd是否相同
        if(newPwd.equals(oldPwd)) return Result.error("新密码不能与原密码相同，请重新输入");

        // (4)判断new_pwd和re_pwd是否一致
        if(!rePwd.equals(newPwd)){
            return Result.error("新密码和确认密码不一致，请重新输入");
        }

        // 2.密码更新
        employeeService.updatePwd(newPwd, id);
        return Result.success();
    }
}
