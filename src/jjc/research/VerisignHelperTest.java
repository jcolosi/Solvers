package jjc.research;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerisignHelperTest {

	static private final String[] data = { "104.223.158.180,amazon.com,1452134050",
			"0.249.14.147,youtube.com,1453743121", "44.139.141.247,facebook.com,1454007141",
			"44.139.141.247,amazon.com,1456174572", "167.18.199.211,facebook.com,1456685329",
			"104.223.158.180,twitter.com,1459337653", "104.223.158.180,facebook.com,1461116253",
			"0.249.14.147,google.com,1463963088", "104.223.158.180,facebook.com,1465739316",
			"44.139.141.247,google.com,1468076743", "150.239.240.25,ebay.com,1478763739" };

	static private VerisignHelper helper;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		helper = new JohnsVerisignHelper();
		helper.loadRecords(data);
	}

	@Test
	public void assertTimestamps() {
		Collection<? extends AbstractRecord> records;
		records = helper.getRecordsByTimestamp();
		showRecords(records);
		// assertTrue(records.get(0));
	}

	@Test
	public void assertCounts() {
		showCounts(helper.getRecordsByIpCount());
	}

	private void showRecords(Collection<? extends AbstractRecord> records) {
		for (AbstractRecord record : records) {
			System.out.println(record);
		}
	}

	private void showCounts(Collection<? extends AbstractIpCount> counts) {
		for (AbstractIpCount count : counts) {
			System.out.println(count);
		}
	}

}
