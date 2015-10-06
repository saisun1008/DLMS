package dlms.common.protocol;

import java.io.Serializable;

import dlms.common.Loan;
import dlms.common.Properties.messageType;
import dlms.common.User;

/**
 * Loan protocol to be sent among servers to communicate user loan info
 * 
 * @author Sai
 *
 */
public class LoanProtocol implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_id;
	private String m_host;
	private int m_port;
	private User m_usr;
	private messageType m_type;

	public LoanProtocol(String id, String host, int port, User usr,
			messageType type)
	{
		m_id = id;
		m_host = host;
		m_port = port;
		m_usr = usr;
		m_type = type;
	}

	public String getId()
	{
		return m_id;
	}

	public void setId(String m_id)
	{
		this.m_id = m_id;
	}

	public String getHost()
	{
		return m_host;
	}

	public void setHost(String m_host)
	{
		this.m_host = m_host;
	}

	public int getPort()
	{
		return m_port;
	}

	public void setPort(int m_port)
	{
		this.m_port = m_port;
	}

	public User getUser()
	{
		return m_usr;
	}

	public void setUser(User m_usr)
	{
		this.m_usr = m_usr;
	}

	public messageType getType()
	{
		return m_type;
	}

}
