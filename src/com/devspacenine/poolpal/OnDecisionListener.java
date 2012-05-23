package com.devspacenine.poolpal;

import android.os.Bundle;

public interface OnDecisionListener {

	public void onPositiveDecision(int requestCode, Bundle values);
	public void onNegativeDecision(int requestCode);
}
