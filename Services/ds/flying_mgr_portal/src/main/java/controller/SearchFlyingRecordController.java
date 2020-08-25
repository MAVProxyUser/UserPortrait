package controller;


import model.AppModel;
import model.SearchFlyingRecordModel;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import util.ResponseTemplate;

/*
 * @Description: 获取历史飞行记录, 供数据平台调用
 * @Author hanks.hu
 * @Date 17/3/29
 */
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class SearchFlyingRecordController {

	/**
	 * 查询符合条件的历史飞行记录的总数目
	 *
	 * @controller /api/flyingRecord/history/total
	 * @method POST
	 * @contentType application/x-www-form-urlencoded
	 * @header appID-string-required-appID
	 * @header secret-string-required-消息体签名
	 * @param orderID-string-optional-飞行编号
	 * @param droneID-string-optional-飞机sn
	 * @param pilot-string-optional-驾驶员skypixel账号名
	 * @param contact-string-optional-驾驶员skypixel填写的联系方式
	 * @param address-string-optional-起飞地点
	 * @param latitude-string-optional-起飞地点纬度
	 * @param longitude-string-optional-起飞地点经度
	 * @param radius-string-optional-区域范围大小(m)
	 * @param droneType-string-optional-飞机型号
	 * @param beginDate-string-optional-开始时间(格式: 2016-12-12 12:12)
	 * @param endDate-string-optional-结束时间(格式: 2016-12-12 12:12)
	 * @param minDuration-number-optional-最短飞行时长(s)
	 * @param maxDuration-number-optional-最长飞行时长(s)
	 * @param minPeakHeight-number-optional-最低的最高点飞行高度(m)
	 * @param maxPeakHeight-number-optional-最高的最高点飞行高度(m)
	 * @param illegal-boolean-optional-违规情况(默认全部, true: 违规, false: 不违规)
	 * @return
	 */
	@RequestMapping(path = "/api/flyingRecord/history/total", method = RequestMethod.POST)
	@ResponseBody
	public String handleGetHistoryTotal(@RequestHeader(name = "appID") String appIDStr, @RequestHeader(name = "secret") String secret, @RequestBody(required = false) String reqBody,
        String orderID, String droneID, String pilot, String contact, String address, String latitude, String longitude, String radius, String droneType,
	    String beginDate, String endDate, Double minDuration, Double maxDuration, Double minPeakHeight, Double maxPeakHeight, Boolean illegal) {

		Integer appID = Integer.parseInt(appIDStr);
		if (!AppModel.isLegalData(appID, reqBody, secret)) {
			return ResponseTemplate.invalidToken();
		}

		JSONObject ret = SearchFlyingRecordModel.getHistoryTotal(orderID, droneID, pilot, contact, address, latitude, longitude, radius, droneType, beginDate, endDate, minDuration,
			maxDuration, minPeakHeight, maxPeakHeight, illegal);
		return ResponseTemplate.responseTemplateJSON(ret);
	}


	/**
	 * 查询符合条件的历史飞行记录
	 *
	 * @controller /api/flyingRecord/history
	 * @method POST
	 * @contentType application/x-www-form-urlencoded
	 * @header appID-string-required-appID
	 * @header secret-string-required-消息体签名
	 * @param pagination-integer-optional-页码(默认首页)
	 * @param orderID-string-optional-飞行编号
	 * @param droneID-string-optional-飞机sn
	 * @param pilot-string-optional-驾驶员skypixel账号名
	 * @param contact-string-optional-驾驶员skypixel填写的联系方式
	 * @param address-string-optional-起飞地点
	 * @param latitude-string-optional-起飞地点纬度
	 * @param longitude-string-optional-起飞地点经度
	 * @param radius-string-optional-区域范围大小(m)
	 * @param droneType-string-optional-飞机型号
	 * @param beginDate-string-optional-开始时间(格式: 2016-12-12 12:12)
	 * @param endDate-string-optional-结束时间(格式: 2016-12-12 12:12)
	 * @param minDuration-number-optional-最短飞行时长(s)
	 * @param maxDuration-number-optional-最长飞行时长(s)
	 * @param minPeakHeight-number-optional-最低的最高点飞行高度(m)
	 * @param maxPeakHeight-number-optional-最高的最高点飞行高度(m)
	 * @param illegal-boolean-optional-违规情况(默认 全部, true: 违规, false: 不违规)
	 * @return
	 */
	@RequestMapping(path = "/api/flyingRecord/history", method = RequestMethod.POST)
	@ResponseBody
	public String handleGetHistory(@RequestHeader(name = "appID") String appIDStr, @RequestHeader(name = "secret") String secret, @RequestBody(required = false) String reqBody,
	   Integer pagination, String orderID, String droneID, String pilot, String contact, String address, String latitude, String longitude, String radius, String droneType,
	   String beginDate, String endDate, Double minDuration, Double maxDuration, Double minPeakHeight, Double maxPeakHeight, Boolean illegal) {

		Integer appID = Integer.parseInt(appIDStr);
		if (!AppModel.isLegalData(appID, reqBody, secret)) {
			return ResponseTemplate.invalidToken();
		}

		JSONObject ret = SearchFlyingRecordModel.getHistory(pagination, orderID, droneID, pilot, contact, address, latitude, longitude, radius, droneType, beginDate, endDate,
			minDuration, maxDuration, minPeakHeight, maxPeakHeight, illegal);
		return ResponseTemplate.responseTemplateJSON(ret);
	}
}
