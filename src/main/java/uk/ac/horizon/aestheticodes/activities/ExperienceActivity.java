/*
 * Aestheticodes recognises a different marker scheme that allows the
 * creation of aesthetically pleasing, even beautiful, codes.
 * Copyright (C) 2013-2015  The University of Nottingham
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import uk.ac.horizon.aestheticodes.Aestheticodes;
import uk.ac.horizon.aestheticodes.R;
import uk.ac.horizon.aestheticodes.controllers.ExperienceFileController;
import uk.ac.horizon.aestheticodes.controllers.ExperienceListController;
import uk.ac.horizon.aestheticodes.model.Experience;
import uk.ac.horizon.aestheticodes.properties.Properties;
import uk.ac.horizon.aestheticodes.properties.bindings.ColorImageBinding;

public class ExperienceActivity extends ActionBarActivity
{
	private ExperienceListController experiences;
	private Experience experience;
	private Properties properties;
	private String experienceID;

	public void editExperience(View view)
	{
		Intent intent = new Intent(ExperienceActivity.this, ExperienceEditActivity.class);
		intent.putExtra("experience", experience.getId());

		startActivity(intent);
	}

	public void shareExperience(View view)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		intent.putExtra(Intent.EXTRA_SUBJECT, experience.getName());
		intent.putExtra(Intent.EXTRA_TEXT, "http://aestheticodes.appspot.com/experience/info/" + experience.getId());
		Intent openInChooser = Intent.createChooser(intent, "Share with...");
		startActivity(openInChooser);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		experienceID = extras.getString("experience");

		setContentView(R.layout.experience);

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		experiences = Aestheticodes.getExperiences();
		experience = experiences.get(experienceID);
		properties = new Properties(this, experience);
		properties.get("name").bindTo(R.id.experienceTitle);
		properties.get("description").bindTo(R.id.experienceDescription);
		properties.get("icon").bindTo(R.id.experienceIcon);
		properties.get("image").bindTo(R.id.experienceImage).bindTo(new ColorImageBinding(R.id.openExperience));
		properties.load();
	}
}