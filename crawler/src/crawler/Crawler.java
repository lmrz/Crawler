package crawler;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

	public int crawler() throws InvalidPropertiesFormatException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, IOException, SQLException {
		String gostr[] = { "互联网", "编程", "算法" };
		String showCategory[] = new String[500];
		String showName[] = new String[500];
		float showScore[] = new float[500];
		int showAssessNum[] = new int[500];
		String showTail[] = new String[500];
		String showPublish[] = new String[500];
		int s = 0;

		File file = new File("D:\\crawler");
		if (!file.exists()) {
			file.mkdir();
		}
		File fileName = new File(file, "crawler.txt");
		if (!fileName.exists()) {
			fileName.createNewFile();
		}
		OutputStream oStream = new FileOutputStream(new File("D:\\crawler\\crawler.txt"));
		@SuppressWarnings("resource")
		BufferedOutputStream bos = new BufferedOutputStream(oStream);
		for (int go = 0; go < 3; go++) {
			for (int ggg = 0; ggg < 10; ggg++) {
				String urlString = "https://book.douban.com/tag/" + gostr[go] + "?start=" + ggg * 20 + "&type=T";
				URL url = new URL(urlString);
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setRequestProperty("contentType", "GB2312");
				htc.setConnectTimeout(3000);
				
				int code = htc.getResponseCode();
				if (code == HttpURLConnection.HTTP_OK) {
					InputStream is = htc.getInputStream();
					InputStreamReader iss = new InputStreamReader(is, "UTF-8");
					BufferedReader bis = new BufferedReader(iss, 500 * 1024);
					@SuppressWarnings("unused")
					byte[] bu = new byte[102400];
					String count;
					String temp = null;
					while ((count = bis.readLine()) != null) {
						String temp1 = count;
						// System.out.println(temp1);
						bu = new byte[102400];
						temp = temp + temp1;
					}
					is.close();
					bis.close();
					iss.close();
					// 拆分网页
					String temp2[] = temp.split("<ul class=\"subject-list\">")[1].split("<div class=\"paginator\">")[0]
							.split("</li>");
					String str1[] = new String[temp2.length - 1];
					String str2[] = new String[temp2.length - 1];
					String str3[] = new String[temp2.length - 1];
					String str4[] = new String[temp2.length - 1];
					float str21[] = new float[temp2.length - 1];
					int str31[] = new int[temp2.length - 1];
					String[] name1 = new String[temp2.length - 1];
					// System.out.println(temp2.length);
					// 获取书名，如失控：全人类的最终命运和结局
					for (int j = 0; j < temp2.length - 1; j++) {
						if (temp2[j].split("</a>")[1].split("onclick").length > 1) {
							String name = temp2[j].split("</a>")[1].split("onclick")[1];
							String reg22 = "(?i).*font.*";
							if (Pattern.compile(reg22).matcher(name.split("\">")[1]).find()) {
								name1[j] = name.split("\">")[1].split("<span")[0].trim()
										+ name.split("\">")[2].split("</span")[0].trim();
								// System.out.println("123");
								// System.out.println(name1[i]);
							} else {
								name1[j] = name.split("\">")[1].split("<span")[0].trim();
							}
							// 获取作者价格等：
							// String str1[]=new String [temp2.length-1];

							str1[j] = temp2[j].split("<div class=\"pub\">")[1].split("</div>")[0].trim();
							// 获取评分 ,评价数
							// System.out.println(temp2[j]);
							// System.out.println(temp2[j].split("clearfix\">")[1].split("</div>")[0].split("</span>")[1]);
							String tt[] = temp2[j].split("clearfix\">")[1].split("</div>")[0].split("</span>");
							// System.out.println(tt.length);
							if (tt.length > 2) {
								str2[j] = temp2[j].split("clearfix\">")[1].split("</div>")[0].split("</span>")[1]
										.split(">")[1].trim();
								str3[j] = temp2[j].split("clearfix\">")[1].split("</div>")[0]
										.split("<span class=\"pl\">")[1].split("</span>")[0].trim();
								@SuppressWarnings("unused")
								String temp1 = "";
								// System.out.println(str3[j]);
								String regEx = "[^0-9]";
								Pattern p = Pattern.compile(regEx);
								Matcher m = p.matcher(str3[j]);
								str21[j] = Float.parseFloat(str2[j]);
								str31[j] = Integer.parseInt(m.replaceAll("").trim());
								str3[j] = String.valueOf(str31[j]);
							} else {

								str2[j] = temp2[j].split("clearfix\">")[1].split("</div>")[0].split("</span>")[0]
										.split(">")[1].trim();
							}
							// 获取结尾文字

							String regEx1 = "(?i).*<p>.*";
							Pattern p1 = Pattern.compile(regEx1);
							Matcher m1 = p1
									.matcher(temp2[j].split("<span class=\"pl\">")[1].split("<div class=\"ft\">")[0]);
							if (m1.find()) {
								str4[j] = temp2[j].split("<span class=\"pl\">")[1].split("<div class=\"ft\">")[0]
										.split("</p>")[0].split("<p>")[1];
							} else {
								str4[j] = null;
							}
							// System.out.println(ggg);
							// System.out.println(j);
							// System.out.println(str2[2]);
							// System.out.println(str21[j]);
							// System.out.println(str31[j]);
							// System.out.println(str4[j]);
							// System.out.println(name1[j]);
							// System.out.println(str1[j]);

						}
					}

					for (int l = 0; l < temp2.length - 1; l++) {

						// String tt = " 分类：" + gostr[go]
						// +System.getProperty("line.separator")+ " 书名：" +
						// name1[l] + System.getProperty("line.separator")+"
						// 出版信息：" + str1[l]
						// + System.getProperty("line.separator")+" 评分：" +
						// str2[l] +"评价数：" + str3[l]
						// +System.getProperty("line.separator")+ " 尾部：" +
						// str4[l]
						// + System.getProperty("line.separator")+ " "+
						// System.getProperty("line.separator");
						// bos.write(tt.getBytes());
						// bos.flush();
						if (str31[l] > 2000) {
							showCategory[s] = gostr[go];
							showName[s] = name1[l];
							showPublish[s] = str1[l];
							showScore[s] = str21[l];
							showAssessNum[s] = str31[l];
							showTail[s] = str4[l];
							String tt = "分类：" + gostr[go] + "    书名：" + name1[l] + "         出版信息：" + str1[l]
									+ "           评分：" + str2[l] + "           评价数：" + str3[l] + "         尾部："
									+ str4[l] + "\r\n";
							System.out.println(tt);
							s++;
						}

					}

				}
			}
		}
       //排序
		float score;
		String category;
		String name;
		int assessNum;
		String tail;
		String publish;

		int size = showScore.length; // 数组大小
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (showScore[i] < showScore[j]) { // 交换两数的位置
					score = showScore[i];
					showScore[i] = showScore[j];
					showScore[j] = score;

					category = showCategory[i];
					showCategory[i] = showCategory[j];
					showCategory[j] = category;

					name = showName[i];
					showName[i] = showName[j];
					showName[j] = name;

					publish = showPublish[i];
					showPublish[i] = showPublish[j];
					showPublish[j] = publish;

					assessNum = showAssessNum[i];
					showAssessNum[i] = showAssessNum[j];
					showAssessNum[j] = assessNum;

					tail = showTail[i];
					showTail[i] = showTail[j];
					showTail[j] = tail;
				}
			}
		}
		for (int i = 0; i < s; i++) {
			System.out.println(" 分类：" + showCategory[i]);
			System.out.println(" 书名：" + showName[i]);
			System.out.println(" 出版信息：" + showPublish[i]);
			System.out.println(" 评分：" + showScore[i]);
			System.out.println(" 评价数：" + + showAssessNum[i]);
			System.out.println("尾部：" + showTail[i]);
			System.out.println();
			String tt = " 分类：" + showCategory[i] + System.getProperty("line.separator") + "   书名：" + showName[i]
					+ System.getProperty("line.separator") + "   出版信息：" + showPublish[i]
					+ System.getProperty("line.separator") + "   评分：" + showScore[i] + "   评价数：" + showAssessNum[i]
					+ System.getProperty("line.separator") + "   尾部：" + showTail[i]
					+ System.getProperty("line.separator") + "  " + System.getProperty("line.separator");

			bos.write(tt.getBytes());
			bos.flush();
		}

		System.out.println(s);
		return 0;

	}
	

	public static void main(String[] args) throws InvalidPropertiesFormatException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		Crawler crawler = new Crawler();
		crawler.crawler();

	}
}
