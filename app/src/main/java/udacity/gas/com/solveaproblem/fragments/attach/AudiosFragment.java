package udacity.gas.com.solveaproblem.fragments.attach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.PailContract;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class AudiosFragment extends Fragment{

	public static int ID = 5;
	private TextView textView;
	private static long PROB_ID;

	public static AudiosFragment getInstance(long position) {
		AudiosFragment myFragment = new AudiosFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, position);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_audio, container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}
		return layout;
	}
}
