<?php
class DB_Connect {

    //constructor
    function __construct() {
        
    }

    //destructor
    function __destruct() {
        // $this->close();
    }

    //数据库连接函数
    public function connect() {
        require_once 'include/config.php';
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
		mysql_query("set names 'utf8'");
        mysql_select_db(DB_DATABASE);
        return $con;
    }

    //关闭数据库连接
    public function close() {
        mysql_close();
    }

}

?>
