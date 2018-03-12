-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2016-01-14 14:11:04
-- 服务器版本： 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `qiandao`
--

-- --------------------------------------------------------

--
-- 表的结构 `formname`
--

CREATE TABLE IF NOT EXISTS `formname` (
  `Key` varchar(8) NOT NULL,
  `FormName` varchar(32) NOT NULL,
  `Author` varchar(11) NOT NULL,
  PRIMARY KEY (`Key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `formname`
--

INSERT INTO `formname` (`Key`, `FormName`, `Author`) VALUES
('0001', '考勤', '15988406002');

-- --------------------------------------------------------

--
-- 表的结构 `group`
--

CREATE TABLE IF NOT EXISTS `group` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `Telnumber` varchar(11) NOT NULL COMMENT '电话',
  `Name` varchar(32) NOT NULL COMMENT '姓名',
  `MyKey` varchar(32) NOT NULL COMMENT '签到口令',
  `FormName` varchar(32) NOT NULL COMMENT '签到组',
  `Power` int(8) NOT NULL COMMENT '签到权限',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户签到权限表' AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `group`
--

INSERT INTO `group` (`ID`, `Telnumber`, `Name`, `MyKey`, `FormName`, `Power`) VALUES
(1, '15988406002', '任永辉', '0001', '考勤', 9);

-- --------------------------------------------------------

--
-- 表的结构 `qiandaobiao`
--

CREATE TABLE IF NOT EXISTS `qiandaobiao` (
  `ID` int(8) NOT NULL AUTO_INCREMENT,
  `Telnumber` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `Date` date DEFAULT NULL,
  `Time` time DEFAULT NULL COMMENT '签到时间',
  `MyKey` varchar(32) NOT NULL COMMENT '签到口令',
  `Contents` varchar(32) NOT NULL COMMENT '签到内容',
  `Conditions` int(8) NOT NULL COMMENT '签到/签离状态',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='签到表' AUTO_INCREMENT=159 ;

--
-- 转存表中的数据 `qiandaobiao`
--

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `telnumber` varchar(11) CHARACTER SET latin1 NOT NULL COMMENT '电话号码',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `sex` varchar(2) NOT NULL COMMENT '性别',
  `password` varchar(32) CHARACTER SET latin1 NOT NULL COMMENT '密码',
  UNIQUE KEY `telnumber` (`telnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`telnumber`, `name`, `sex`, `password`) VALUES
('15988406002', '任永辉', '男', 'c4ca4238a0b923820dcc509a6f75849b'),


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
