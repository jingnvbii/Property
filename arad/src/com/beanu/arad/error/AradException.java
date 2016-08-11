package com.beanu.arad.error;

import android.content.res.Resources;
import android.text.TextUtils;

import com.beanu.arad.Arad;
import com.beanu.arad.R;

/**
 * API接口，异常错误处理
 * 
 * <pre>
 * 需要在string.xml中添加错误描述,以 code+错误代码 的形式出现
 * </pre>
 */
public class AradException extends Exception {

	private static final long serialVersionUID = -6055929793464170833L;
	private String error;
	private String oriError;
	private String error_code;

	public AradException() {
	}

	public AradException(String detailMessage) {
		error = detailMessage;
	}

	public AradException(String detailMessage, Throwable throwable) {
		error = detailMessage;
	}

	private String getError() {

		String result;

		if (!TextUtils.isEmpty(error)) {
			result = error;
		} else {

			String name = "code" + error_code;
			int i = Arad.app.getResources().getIdentifier(name, "string", Arad.app.getPackageName());

			try {
				result = Arad.app.getString(i);

			} catch (Resources.NotFoundException e) {

				if (!TextUtils.isEmpty(oriError)) {
					result = oriError;
				}

				/***************烟台项目显示的异常code*********************************/
				else if(error_code.equals("001")){
					result = "操作失败";
				}
				else if(error_code.equals("002")){
					result = "账号或密码不正确,请重新输入!";
				}
				else if(error_code.equals("003")){
					result = "登录成功";
				}
				else if(error_code.equals("004")){
					result = "该手机号已经注册过了";
				}
				else if(error_code.equals("005")){
					result = "该手机号尚未注册";
				}
				else if(error_code.equals("006")){
					result = "无数据";
				}
				else if(error_code.equals("007")){
					result = "该帖子已收藏";
				}
				else if(error_code.equals("008")){
					result = "帖子分类不存在";
				}
				else if(error_code.equals("009")){
					result = "缺少参数";
				}
				else if(error_code.equals("010")){
					result = "该帖子已取消收藏";
				}
				else if(error_code.equals("011")){
					result = "该帖子不存在";
				}
				else if(error_code.equals("012")){
					result = "已取消收藏,无需重复取消";
				}
				else if(error_code.equals("013")){
					result = "该商家已收藏";
				}
				else if(error_code.equals("014")){
					result = "要回复的楼层已被删除";
				}
				else if(error_code.equals("015")){
					result = "更新订单状态失败";
				}
				else if(error_code.equals("016")){
					result = "新增会员消费记录失败";
				}
				else if(error_code.equals("017")){
					result = "更新商品库存失败";
				}
				else if(error_code.equals("018")){
					result = "更新会员积分失败";
				}
				else if(error_code.equals("019")){
					result = "新增会员消费记录失败";
				}
				else if(error_code.equals("020")){
					result = "未找到匹配用户";
				}
				else if(error_code.equals("021")){
					result = "新增消息通知失败";
				}
				else if(error_code.equals("022")){
					result = "已签到,无需重复签到";
				}
				else if(error_code.equals("023")){
					result = "现金券已用完，无法完成赠送！";
				}
				else if(error_code.equals("024")){
					result = "未获取到用户信息！";
				}
				else if(error_code.equals("025")){
					result = "商家余额更新失败！";
				}
				else if(error_code.equals("026")){
					result = "增加商家收支记录失败！";
				}
				else if(error_code.equals("027")){
					result = "已经拉黑过,无需重复拉黑！";
				}
				else if(error_code.equals("028")){
					result = "已经点过赞了";
				}
				else if(error_code.equals("029")){
					result = "该订单已支付，请勿重复操作！";
				}
				else if(error_code.equals("030")){
					result = "已经取消收藏了,无需重复取消！";
				}
				else if(error_code.equals("031")){
					result = "已经收藏了,无需重复收藏！";
				}
				else if(error_code.equals("032")){
					result = "管理员不能被加入黑名单！";
				}else if(error_code.equals("033")){
					result = "该设备已被拉黑!";
				}else if(error_code.equals("034")){
					result = "请验证手机号!";
				}else if(error_code.equals("035")){
					result = "请先进行注册!";
				}
                else {
                    result = Arad.app.getString(R.string.unknown_error_code) + error_code;
                }
			}
		}

		return result;
	}

	@Override
	public String getMessage() {
		return getError();
	}

	/**
	 * 设置错误代码
	 * 
	 * @param error_code
	 */
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_code() {
		return error_code;
	}

	/**
	 * 如果没有错误代码，可以抛出原声的错误信息
	 * 
	 * @param oriError
	 */
	public void setOriError(String oriError) {
		this.oriError = oriError;
	}

}
