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
/****************员工端显示的异常code****************************************************************************/





/***************银丰员工端显示的异常code*********************************/
				/*else if(error_code.equals("001")){
					result = "操作失败";
				}
				else if(error_code.equals("002")){
					result = "无数据";
				}
				else if(error_code.equals("003")){
					result = "请求参数不正确";
				}
				else if(error_code.equals("004")){
					result = "更新上下班状态时异常";
				}
				else if(error_code.equals("005")){
					result = "用户名名或者密码错误";
				}
				else if(error_code.equals("006")){
					result = "未获取到员工信息";
				}
				else if(error_code.equals("007")){
					result = "原登录密码不正确";
				}
				else if(error_code.equals("008")){
					result = "未获取到帖子相关信息";
				}
				else if(error_code.equals("009")){
					result = "通知公告已经签收过";
				}
				else if(error_code.equals("010")){
					result = "单据已经被抢";
				}
				else if(error_code.equals("011")){
					result = "未获取到巡更巡查相关信息";
				}
				else if(error_code.equals("012")){
					result = "未获取到巡更点相关信息";
				}
				else if(error_code.equals("013")){
					result = "当前时间不在巡查时间内，不可继续巡更";
				}
				else if(error_code.equals("014")){
					result = "当前巡查点已经巡查过，不可重复巡查";
				}
				else if(error_code.equals("015")){
					result = "已经巡查结束和未完成的不可继续巡查";
				}
				else if(error_code.equals("016")){
					result = "所有的巡查点已经巡查完毕，不可继续巡查";
				}
				else if(error_code.equals("017")){
					result = "";
				}
				else if(error_code.equals("018")){
					result = "查询二维码信息失败！";
				}
				else if(error_code.equals("019")){
					result = "未获取到报修信息！";
				}
				else if(error_code.equals("020")){
					result = "该工单已被其他员工抢先一步，请下次再来！";
				}
				else if(error_code.equals("021")){
					result = "工单的状态已经发生变更，请刷新后再操作！";
				}
				else if(error_code.equals("022")){
					result = "该报修已经反馈过，请勿重复操作！";
				}*/
/****************员工端显示的异常code****************************************************************************/

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
