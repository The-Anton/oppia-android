package org.oppia.android.app.devoptions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.oppia.android.R
import org.oppia.android.app.activity.InjectableAppCompatActivity
import org.oppia.android.app.drawer.KEY_NAVIGATION_PROFILE_ID
import javax.inject.Inject

const val MARK_CHAPTERS_COMPLETED_FRAGMENT = "MARK_CHAPTERS_COMPLETED_FRAGMENT"
const val MARK_STORIES_COMPLETED_FRAGMENT = "MARK_STORIES_COMPLETED_FRAGMENT"
const val MARK_TOPICS_COMPLETED_FRAGMENT = "MARK_TOPICS_COMPLETED_FRAGMENT"
const val EVENT_LOGS_FRAGMENT = "EVENT_LOGS_FRAGMENT"
const val FORCE_NETWORK_TYPE_FRAGMENT = "FORCE_NETWORK_TYPE_FRAGMENT"

/** Activity for Developer Options. */
class DeveloperOptionsActivity : InjectableAppCompatActivity(), ForceCrashButtonClickListener {
  @Inject
  lateinit var developerOptionsActivityPresenter: DeveloperOptionsActivityPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activityComponent.inject(this)
    developerOptionsActivityPresenter.handleOnCreate()
    title = getString(R.string.developer_options_activity_title)
  }

  companion object {
    /** Function to create intent for DeveloperOptionsActivity */
    fun createDeveloperOptionsActivityIntent(context: Context, internalProfileId: Int): Intent {
      val intent = Intent(context, DeveloperOptionsActivity::class.java)
      intent.putExtra(KEY_NAVIGATION_PROFILE_ID, internalProfileId)
      return intent
    }

    fun getIntentKey(): String {
      return KEY_NAVIGATION_PROFILE_ID
    }
  }

  override fun forceCrash() {
    developerOptionsActivityPresenter.forceCrash()
  }
}
