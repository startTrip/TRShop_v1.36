package shop.trqq.com.bean;

import java.util.ArrayList;

public class VoiceBean {
	public ArrayList<WSBean> ws;

	public class WSBean {
		public ArrayList<CWBean> cw;
	}

	public class CWBean {
		public String w;
	}
}
