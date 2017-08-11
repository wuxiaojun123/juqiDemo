/**
 * 球局列表
 */
package cn.com.sfn.juqi.dejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.MatchModel;

public class ManageListDejson {
	private List<MatchModel> matchList;
	private String matches;

	public List<MatchModel> matchListDejson(String str) {
		matchList = new ArrayList<MatchModel>();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return matchList;
			} else {
				matches = jObject.getString("data");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(matches);
			for (int i = 0; i < jsonArray.length(); i++) {
				MatchModel matchModel = new MatchModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				matchModel.setId(json.getString("id"));
				matchModel.setTitle(json.getString("title"));
				matchModel.setU_id(json.getString("u_id"));
				matchModel.setU_mobile(json.getString("u_mobile"));
				matchModel.setAttendance(json.getString("attendance"));
				matchModel.setNum(json.getString("num"));
				matchModel.setDuration(json.getString("duration"));
				matchModel.setCreate_time(json.getString("create_time"));
				matchModel.setStart_time(json.getString("start_time"));
				matchModel.setSpec(json.getString("spec"));
				matchModel.setDetail(json.getString("detail"));
				matchModel.setS_name(json.getString("s_name"));
				matchModel.setLocation(json.getString("location"));
				matchModel.setLongitude(Double.valueOf(json
						.getString("longitude")));
				matchModel.setLatitude(Double.valueOf(json
						.getString("latitude")));
				matchModel.setFee(json.getString("fee"));
				matchModel.setStatus(json.getString("status"));
//				matchModel.setUserAndmatch(json.getString("userAndmatch"));

				matchList.add(matchModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return matchList;
	}

	private List<MatchModel> cerList;
	private String certificates;

	public List<MatchModel> cetificateDejson(String str) {
		cerList = new ArrayList<MatchModel>();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return cerList;
			} else {
				certificates = jObject.getString("data");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(certificates);
			for (int i = 0; i < jsonArray.length(); i++) {
				MatchModel matchModel = new MatchModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				matchModel.setAttendID(json.getString("id"));
				matchModel.setId(json.getString("g_id"));
				matchModel.setTitle(json.getString("g_name"));
				matchModel.setDuration(json.getString("g_duration"));
				matchModel.setStart_time(json.getString("g_starttime"));
				matchModel.setS_name(json.getString("s_name"));
				matchModel.setStatus(json.getString("g_status_title"));
				cerList.add(matchModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cerList;
	}
}