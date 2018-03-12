<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {
	$tag = $_POST['tag'];

    //实例化对象
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    //根据$tag的值调用不同的函数
    if ($tag == 'login') {//*****************************************************登录
        $telnumber = $_POST['telnumber'];
        $password = $_POST['password'];

        //调用登录函数
        $user = $db->Login($telnumber, $password);
        if ($user != false) {
            echo "success";
        } else {
            echo "failed";
        }
    } else if ($tag == 'register') {//********************************************注册
        $telnumber=$_POST['telnumber'];
        $name = $_POST['name'];
		$sex=$_POST['sex'];
        $password = $_POST['password'];

        //用户是否已存在
        if ($db->isUserExisted($telnumber)) {
            echo "exist";
        } else {
            //存储新用户信息
            $user = $db->Register($telnumber,$name,$sex,$password);
            if ($user) {
                echo "success";
            } else {
                echo "failed";
            }
        }
    } else if($tag=='come_go'){//************************************************签到/签离
	    $telnumber=$_POST['telnumber'];
        $mykey = $_POST['key'];
		$condition=$_POST['condition'];
		$user = $db->SignIn($telnumber,$mykey,$condition);
		echo $user;
    }else if($tag=='modify'){//************************************************修改密码
		$telnumber=$_POST['telnumber'];
		$password = $_POST['password'];
		$new_password=$_POST['newpassword'];
		$user = $db->Modify($telnumber,$password,$new_password);
		if($user){
			echo "success";
		}else{
			echo "failed";
		}
	}else if($tag=='search'){//*************************************************签到查询
		$telnumber = $_POST['telnumber'];		
        $user = $db->SignInSearch($telnumber);		
        if($user){
			echo json_encode($user);			
        }else{
			echo "failed";		 
        }	   
    }else if($tag=="all_search"){//*******************************************查看成员签到
		$telnumber = $_POST['telnumber'];
		$key = $_POST['key'];
		$date=$_POST['date'];
		$user = $db->AllSearch($telnumber,$key,$date);
		if($user&&$user!="no"){
			echo json_encode($user);			
        }else if($user=="no"){
			echo "no";
		}else{
			echo "failed";		 
        }	
	}else if($tag=='add'){//**********************************************新建签到
	    $contents = $_POST['content'];
		$mykey=$_POST['mykey'];
		$author=$_POST['telnumber'];
		if($contents!=null&&$mykey!=null&&$author!=null){
		    $user=$db->Add($contents,$mykey,$author);
		}else{
			$user="failed";
		}
		switch($user){
			case "success":
			    echo "success";
			    break;
		    case "exist":
		        echo "exist";
			    break;
		    default:
		        echo "failed";
			    break;
		}
	}else if($tag=='chaxun'){//******************************************状态查询
		$telnumber=$_POST['telnumber'];
		$key=$_POST['key'];
		if($telnumber!=null&&$key!=null){
		    $user=$db->ChaXun($telnumber,$key);
		}else{
			$user=3;
		}
		switch($user){
			case 1:
			    echo "签到";
				break;
			case 2:
			    echo "签离";
				break;
			default:
			    echo "failed";
			    break;
		}
	}else if($tag=='apply'){//**********************************************申请加入签到组
		$telnumber=$_POST['telnumber'];
		$key=$_POST['keys'];
		if($telnumber!=null&&$key!=null){
			$user=$db->Apply($key,$telnumber);
		}else{
			$user="failed";
		}
		echo $user;
	}else if($tag=="apply_search"){//*********************************************查看组员
		$telnumber=$_POST['telnumber'];
		$key=$_POST['key'];
		if($telnumber!=null&&$key!=null){
			$user=$db->Apply_Search($key,$telnumber);
			if($user){
				echo json_encode($user);
			}else{
			    echo "failed";
		    }
		}else{
			echo "failed";
		}
	}else if($tag=="power_change"){//**********************************************修改权限
		$telnumber=$_POST['telnumber'];
		$key=$_POST['key'];
		$power=$_POST['power'];
		$op=$_POST['op'];
		$user=$db->PowerChange($key,$telnumber,$op,$power);
		echo $user;
	}else if($tag=="delete"){//*****************************************************删除成员
		$telnumber=$_POST['telnumber'];
		$key=$_POST['key'];
		$op=$_POST['op'];
		$user=$db->Deletes($key,$telnumber,$op);
		echo $user;
	}else if($tag=="my_group"){//**************************************************我的签到组
		$telnumber=$_POST['telnumber'];
		$user=$db->GroupSearch($telnumber);
		echo json_encode($user);
	}else{
	    echo "failed";
	}

} else {
    echo "failed";
}
?>
