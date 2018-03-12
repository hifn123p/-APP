<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
		//连接数据库
        require_once 'DB_Connect.php';
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    //注册
    public function Register($telnumber,$name, $sex, $password) {
        $password=md5($password);
        $result = mysql_query("INSERT INTO users( telnumber, name, sex, password) 
		    VALUES('$telnumber', '$name', '$sex', '$password')");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    //检查电话号码是否已被注册
    public function isUserExisted($telnumber) {
        $result = mysql_query("SELECT * FROM users WHERE telnumber = '$telnumber'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            return true;
        } else {
            return false;
        }
    }
	
    //登录
    public function Login($telnumber, $password) {
        $result1 = mysql_query("SELECT * FROM users WHERE telnumber = '$telnumber'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result1);
        if ($no_of_rows > 0) {
            $result2 = mysql_fetch_array($result1);
            $psd = $result2['password'];
			$password=md5($password);
            if ($password==$psd) {
                return true;
            }else{
				return false;
			}
        } else {
            return false;
        }
    }
	
	//修改密码
	public function Modify($telnumber,$password,$new_password){
		 $new_password=md5($new_password);
		 $result1 = mysql_query("SELECT * FROM users WHERE telnumber = '$telnumber'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result1);
        if ($no_of_rows > 0) {
            $result2 = mysql_fetch_array($result1);
            $psd = $result2['password'];
			$password=md5($password);
            if ($password==$psd) {
				$result3 = mysql_query("update users set password = '$new_password' WHERE telnumber='$telnumber'");
				if($result3)
                  return true;
            }else{
				return false;
			}
        } else {
            return false;
        }
     
	}
	
	//签到状态查询
	public function ChaXun($telnumber,$key){
		$result=mysql_query("SELECT * FROM qiandaobiao WHERE Telnumber='$telnumber' AND MyKey='$key' ORDER BY DATE DESC,Time DESC")or die(mysql_error());
		$num=mysql_num_rows($result);
		if($num>0){
			$array=mysql_fetch_array($result);
		    $condition=$array['Conditions'];
		    return $condition;
		}else{
			$result1=mysql_query("SELECT * FROM formname WHERE `Key` = '$key'")or die(mysql_error());
			$num1=mysql_num_rows($result1);
			if($num1>0){
				return 2;
			}else{
			    return 3;
			}
		}
	}
	
    
    //签到、签离
	public function SignIn($telnumber,$key,$condition){
		$timezone_identifier = "PRC";  //本地时区标识
        date_default_timezone_set($timezone_identifier);
		$Date=date("Y-m-d");
		$Time=date("H:i:s");
		
		$result=mysql_query("SELECT * FROM `group` WHERE Telnumber='$telnumber' and MyKey='$key'")or die(mysql_error());
		$arr=mysql_fetch_array($result);
		$power=$arr["Power"];
		if($power==null||$power==0){
			return "no";
		}
		
		if($condition=="签到"){
            $flag=1;
		}else if($condition=="签离"){
			$flag=2;
		}else{
			return "failed";
		}
		
		$result=mysql_query("SELECT * FROM qiandaobiao WHERE MyKey = '$key' AND Telnumber='$telnumber' ORDER BY DATE DESC,Time DESC")or die(mysql_error());
		$arr=mysql_fetch_array($result);
		if($flag==$arr['Conditions']){
			return "touch_again";
		}
		
		$result=mysql_query("SELECT * FROM users WHERE Telnumber='$telnumber'")or die(mysql_error());
		$arr=mysql_fetch_array($result);
		$name=$arr["name"];
		
		//获取签到内容
		$result1=mysql_query("SELECT * FROM formname WHERE `Key` = '$key'")or die(mysql_error());
		$array=mysql_fetch_array($result1);
		$Contents=$array['FormName'];
		//判断口令是存在
		if($Contents!=null){
			$result2=mysql_query("INSERT INTO qiandaobiao(Telnumber,Date,Time,MyKey,Contents,Name,Conditions) 
			    VALUES ('$telnumber','$Date','$Time','$key','$Contents','$name',$flag)")or die(mysql_error());
			if($result2){
					return "success";
				}else{
					return "failed";
				}
		}else{
			return "error";//口令错误
		}	
	}
	
	
	//签到查询
	public function SignInSearch($telnumber){
		$table = "qiandaobiao";		
        $array = array();		
        $result = mysql_query("SELECT * FROM `$table` WHERE Telnumber = '$telnumber'")or die(mysql_error());
        while($rs = mysql_fetch_array($result))	{
			$rs['Contents'] = base64_encode($rs['Contents']);
			$array[] = $rs;	
        }		
        return $array;
    }
	
	//查看成员签到
	public function AllSearch($telnumber,$key,$date){
		//判断查看者权限
		$result = mysql_query("SELECT * FROM `group` WHERE MyKey = '$key' AND Telnumber='$telnumber'")or die(mysql_error());
		$rs = mysql_fetch_array($result);
		if($rs['Power']<5){
			return "no";
		}
		
        $array = array();		
        $result = mysql_query("SELECT * FROM qiandaobiao WHERE MyKey = '$key' AND Date='$date'")or die(mysql_error());
        while($rs = mysql_fetch_array($result))	{
			$rs['Name'] = base64_encode($rs['Name']);
			$array[] = $rs;	
        }		
        return $array;
    }
	
	
	//新建签到
	public function Add($contents,$mykey,$author){
	    $result=mysql_query("SELECT * FROM formname WHERE `Key`='$mykey'")or die(mysql_error());
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
		    return "exist";
		}else{
			$result1=mysql_query("SELECT * FROM users WHERE telnumber='$author'")or die(mysql_error());
			$arr=mysql_fetch_array($result1);
			$name=$arr["name"];
		    $result2=mysql_query("INSERT INTO formname(`Key`,FormName,Author) VALUES ('$mykey','$contents','$author')")or die(mysql_error());
			if($result2){
				$result3=mysql_query("INSERT INTO `group`(MyKey,formname,Telnumber,Name,Power) VALUES ('$mykey','$contents','$author','$name',9)");
				if($result3){
					return "success";
				}else{
					$result4=mysql_query("DELETE FROM formname WHERE `Key` = '$mykey' AND FormName = '$contents' AND Author = '$author'")or die(mysql_error());
					return "failed";
				}
			}else{
			    return "failed";
			}
		}
	}
	
	//申请加入签到组
	public function Apply($key,$telnumber){
		//是否已经申请过
		$result=mysql_query("SELECT * from `group` where Telnumber = '$telnumber' and MyKey = '$key'")or die(mysql_error());
		$no_of_rows = mysql_num_rows($result);
		if($no_of_rows>0){
			return "exist";//已申请过
		}else{//未申请过则加入权限表中
			$result1=mysql_query("SELECT * from formname where `key` = '$key'")or die(mysql_error());
			$no_of_rows=mysql_num_rows($result1);
			if($no_of_rows>0){
				$result2=mysql_fetch_assoc($result1);
			    $FormName=$result2['FormName'];
			    $result3=mysql_query("SELECT * from users where telnumber = '$telnumber'")or die(mysql_error());
				$result4=mysql_fetch_assoc($result3);
			    $name=$result4['name'];
				$result5=mysql_query("INSERT INTO `group`(MyKey,FormName,Telnumber,Name,Power) VALUES ('$key','$FormName','$telnumber','$name',0)")or die(mysql_error());
				if($result5){
					return "success";
				}else{
					return "failed";
				}
			}else{
				return "error";//口令错误
			}
		}
		
	}
	
	//我的签到组
	public function GroupSearch($telnumber){
		$result=mysql_query("SELECT * from `group` where Telnumber = '$telnumber' and Power >=1")or die(mysql_error());
		$array = array();
		while($rs = mysql_fetch_array($result))	{
				$rs['FormName'] = base64_encode($rs['FormName']);
				$rs['MyKey'] = base64_encode($rs['MyKey']);
				$array[] = $rs;	
			}		
			return $array;
	}
	
	
	//查看组员
	public function Apply_Search($key,$telnumber){
        $array = array();		
		$result1=mysql_query("select * from `group` where MyKey = '$key' and telnumber = '$telnumber'")or die (mysql_error());
		$arr=mysql_fetch_array($result1);
		if($arr['Power']>=1){
			$result = mysql_query("SELECT * FROM `group` WHERE MyKey = '$key'")or die(mysql_error());
			while($rs = mysql_fetch_array($result))	{
				$rs['FormName'] = base64_encode($rs['FormName']);
				$rs['Name'] = base64_encode($rs['Name']);
				$array[] = $rs;	
			}		
			return $array;
		}else{
			return false;
		}
	}
	
	//修改权限
	public function PowerChange($key,$Mtelnumber,$op,$power){
		if($op=="[]"){
			return "no data";
		}
		$con = strlen($op);//获取字符串长度
		$op = substr($op,1,$con-2);//去除中括号
		$op=str_replace(" ","",$op);//去除字符串中的空格
		$op = explode(",",$op);//将字符串按逗号分割为数组
		//查询操作者权限
		$result1=mysql_query("select * from `group` where mykey = '$key' and telnumber = '$Mtelnumber'")or die (mysql_error());
		$arr1=mysql_fetch_array($result1);
		$con=count($op);//数组长度
		if($arr1['Power']==9){//操作者为组长
			$i=0;
			while($i<$con){
				if($Mtelnumber!=$op[$i]){//组长不能操作自己
				    $result2=mysql_query("update `group` set Power = $power where MyKey = '$key' and Telnumber = '$op[$i]'")or die (mysql_error());
				    if($result2){
					    $i++;
				    }else{
					    return "failed";
				    }
				}
			}
			return "success";
		}else if($arr1['Power']==5){//操作者为管理员
		    if($power==5)
		    {
			    return "no";
		    }
			$j=0;
			while($j<$con){
				$telnumber = $op[$j];
				$result2=mysql_query("select * from `group` where mykey = '$key' and telnumber = '$telnumber'")or die (mysql_error());
				$arr2=mysql_fetch_array($result2);
				if($arr2['Power']<5){//判断被操作者权限
					$result3=mysql_query("update `group` set power = '$power' where mykey = '$key' and telnumber = '$telnumber'")or die (mysql_error());
					if($result3){
						$j++;
					}else{
						return "failed";
					}
				}else{
					return "no";
				}
			}
			return "success";
			
		}else{//其他人无权限操作
			return "no";
		}
	}
	
	//删除成员
	public function Deletes($key,$Mtelnumber,$op){
		if($op=="[]"){
			return "no data";
		}
		$con = strlen($op);//获取字符串长度
		$op = substr($op,1,$con-2);//去除中括号
		$op=str_replace(" ","",$op);//去除字符串中的空格
		$op = explode(",",$op);//将字符串按逗号分割为数组
		//查询操作者权限
		$result1=mysql_query("select * from `group` where mykey = '$key' and telnumber = '$Mtelnumber'")or die (mysql_error());
		$arr1=mysql_fetch_array($result1);
		$con=count($op);//数组长度
		if($arr1['Power']==9){//操作者为组长
			$i=0;
			while($i<$con){
				if($Mtelnumber!=$op[$i]){//组长不能操作自己
				    $result2=mysql_query("DELETE FROM `group` WHERE MyKey = '$key' and Telnumber = '$op[$i]'")or die (mysql_error());
				    if($result2){
					    $i++;
				    }else{
					    return "failed";
				    }
				}
			}
			return "success";
		}else if($arr1['Power']==5){//操作者为管理员
			$j=0;
			while($j<$con){
				$telnumber = $op[$j];
				$result2=mysql_query("select * from `group` where mykey = '$key' and telnumber = '$telnumber'")or die (mysql_error());
				$arr2=mysql_fetch_array($result2);
				if($arr2['Power']<5){//判断被操作者权限
					$result3=mysql_query("DELETE FROM `group` WHERE MyKey = '$key' and Telnumber = 'telnumber'")or die (mysql_error());
					if($result3){
						$j++;
					}else{
						return "failed";
					}
				}else{
					return "no";
				}
			}
			return "success";
			
		}else{//其他人无权限操作
			return "no";
		}
	}
	
	
	
}

?>
