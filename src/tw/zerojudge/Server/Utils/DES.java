package tw.zerojudge.Server.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import tw.zerojudge.Server.Configs.ConfigFactory;

public class DES {

	/** 加密、解密key. */
	private final String PASSWORD_CRYPT_KEY;

	/** 加密算法 */
	private final String ALGORITHM = "DES/CBC/PKCS5Padding";

	public DES() {
		PASSWORD_CRYPT_KEY = ConfigFactory.getServerConfig().getCryptKey();
	}

	/**
	 * 
	 * 对数据进行DES加密.
	 * 
	 * @param data
	 *            待进行DES加密的数据
	 * 
	 * @return 返回经过DES加密后的数据
	 * 
	 * @throws Exception
	 */
	public final String decrypt(String data) throws Exception {
		return new String(decrypt(hex2byte(data.getBytes()),
				PASSWORD_CRYPT_KEY.getBytes()), "UTF-8");
	}

	/**
	 * 
	 * 对用DES加密过的数据进行解密.
	 * 
	 * @param data
	 *            DES加密数据
	 * 
	 * @return 返回解密后的数据
	 * 
	 * @throws Exception
	 */

	public final String encrypt(String data) throws Exception {
		return byte2hex(encrypt(data.getBytes("UTF-8"),
				PASSWORD_CRYPT_KEY.getBytes()));
	}

	/**
	 * 
	 * 用指定的key对数据进行DES加密.
	 * 
	 * @param data
	 *            待加密的数据
	 * 
	 * @param key
	 *            DES加密的key
	 * 
	 * @return 返回DES加密后的数据
	 * 
	 * @throws Exception
	 */

	private byte[] encrypt(byte[] data, byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		IvParameterSpec iv = new IvParameterSpec(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
		return cipher.doFinal(data);
	}

	/** */
	/**
	 * 
	 * 用指定的key对数据进行DES解密.
	 * 
	 * @param data
	 *            待解密的数据
	 * 
	 * @param key
	 *            DES解密的key
	 * 
	 * @return 返回DES解密后的数据
	 * 
	 * @throws Exception
	 */

	private byte[] decrypt(byte[] data, byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		IvParameterSpec iv = new IvParameterSpec(key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		return cipher.doFinal(data);
	}

	public byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public String byte2hex(byte[] b) {
		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}

}
