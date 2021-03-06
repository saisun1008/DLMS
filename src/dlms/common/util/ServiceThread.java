package dlms.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceThread extends Thread {

	private String m_cmd = "";
	private Process m_process = null;

	public ServiceThread(String cmd) {
		m_cmd = cmd;
	}

	@Override
	public void run() {
		runCmd(m_cmd);
	}

	/**
	 * Cleanup the running process, VERY important to release used resources
	 */
	public void cleanup() {
		m_process.destroy();
	}

	/**
	 * Execute command, mainly designed to use under Windows, NEED to verify
	 * under Linux or OSX
	 *
	 * @param cmd
	 * @return
	 */
	private String runCmd(String cmd) {
		StringBuilder builder = new StringBuilder();

		try {
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				System.out.println("cmd /c " + cmd);
				m_process = Runtime.getRuntime().exec("cmd /c " + cmd);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(m_process.getInputStream()));
			}
			else
			{
				Process p = Runtime.getRuntime().exec(cmd);                                                                                                                                                     
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String s;
				while ((s = stdInput.readLine()) != null) {
				        System.out.println(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}
