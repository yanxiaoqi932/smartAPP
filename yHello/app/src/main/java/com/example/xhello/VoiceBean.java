package com.example.xhello;

import java.util.ArrayList;

public class VoiceBean {

	public ArrayList<WsBean> ws;

	public class WsBean {
		public ArrayList<CwBean> cw;
	}

	public class CwBean {
		public String w;
	}
}
