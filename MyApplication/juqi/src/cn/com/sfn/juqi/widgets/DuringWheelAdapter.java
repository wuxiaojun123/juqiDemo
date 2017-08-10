package cn.com.sfn.juqi.widgets;

/**
 * During Wheel adapter.
 */
public class DuringWheelAdapter implements WheelAdapter {

	/** The default min value */
	public static final double DEFAULT_MAX_VALUE = 8;

	/** The default max value */
	private static final double DEFAULT_MIN_VALUE = 0.5;

	// Values
	private double minValue;
	private double maxValue;

	// format
	private String format;

	/**
	 * Default constructor
	 */
	public DuringWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 */
	public DuringWheelAdapter(double minValue, double maxValue) {
		this(minValue, maxValue, null);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 * @param format
	 *            the format string
	 */
	public DuringWheelAdapter(double minValue, double maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			double value = 0.5 * (index + 1);
			return format != null ? String.format(format, value) : Double
					.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return (int) (maxValue * 2);
	}

	@Override
	public int getMaximumLength() {
		int max = (int) Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}