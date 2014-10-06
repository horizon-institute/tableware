/*
 * Aestheticodes recognises a different marker scheme that allows the
 * creation of aesthetically pleasing, even beautiful, codes.
 * Copyright (C) 2014  Aestheticodes
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.horizon.aestheticodes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.squareup.picasso.Picasso;
import uk.ac.horizon.aestheticodes.R;
import uk.ac.horizon.aestheticodes.detect.ExperienceEventListener;
import uk.ac.horizon.aestheticodes.model.Experience;
import uk.ac.horizon.aestheticodes.model.ExperienceManager;
import uk.ac.horizon.aestheticodes.model.Marker;
import uk.ac.horizon.aestheticodes.model.MarkerAction;
import uk.ac.horizon.aestheticodes.settings.ActivitySettingsItem;
import uk.ac.horizon.aestheticodes.settings.AddMarkerSettingsItem;
import uk.ac.horizon.aestheticodes.settings.MarkerSettingsItem;
import uk.ac.horizon.aestheticodes.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExperienceActivity extends SettingsActivity implements ExperienceEventListener
{
	private ExperienceManager experienceManager;
	private Experience experience;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.i("", getIntent().toString());
		String experienceID = getIntent().getData().getHost();

		experienceManager = ExperienceManager.get(this);
		experience = experienceManager.get(experienceID);

		getSupportActionBar().setTitle(getString(R.string.marker_title, experience.getName()));
		if (experience.getIcon() != null)
		{
			Picasso.with(this).load(experience.getIcon()).into(new ActionBarTarget(this));
		}

		String code = getIntent().getData().getLastPathSegment();
		Log.i("", "Code: " +  code);
		if (code != null && !code.isEmpty())
		{
			final AddMarkerSettingsItem.AddMarkerDialogFragment dialogFragment = new AddMarkerSettingsItem.AddMarkerDialogFragment();
			final Bundle bundle = new Bundle();
			bundle.putString("experience", experience.getId());
			bundle.putString("code", code);
			dialogFragment.setArguments(bundle);
			dialogFragment.show(getSupportFragmentManager(), "missiles");
		}

		refresh();
	}

	@Override
	public void refresh()
	{
		adapter.clear();
		final List<MarkerAction> actions = new ArrayList<MarkerAction>(experience.getMarkers().values());
		Collections.sort(actions, new Comparator<MarkerAction>()
		{
			@Override
			public int compare(MarkerAction markerAction, MarkerAction markerAction2)
			{
				return markerAction.getCode().compareTo(markerAction2.getCode());
			}
		});
		for (MarkerAction action : actions)
		{
			if (action.isVisible())
			{
				adapter.add(new MarkerSettingsItem(this, experience, action));
			}
		}

		if (experience.canAddMarker())
		{
			adapter.add(new AddMarkerSettingsItem(this, experience, getString(R.string.marker_add)));
		}

		if(experience.isEditable())
		{
			Intent intent = new Intent(this, MarkerSettingsActivity.class);
			intent.putExtra("experience", experience.getId());

			adapter.add(new ActivitySettingsItem(this, getString(R.string.settings), intent));
		}

		Intent intent = new Intent(this, AboutActivity.class);

		adapter.add(new ActivitySettingsItem(this, getString(R.string.about), intent));

		adapter.notifyDataSetChanged();
	}

	@Override
	public void saveChanges()
	{
		experienceManager.add(experience);
	}

	@Override
	public void experienceSelected(Experience experience)
	{

	}

	@Override
	public void experiencesChanged()
	{
		refresh();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		experienceManager.removeListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		experienceManager.addListener(this);
		refresh();
	}

	@Override
	public void markersFound(List<Marker> markers)
	{

	}
}