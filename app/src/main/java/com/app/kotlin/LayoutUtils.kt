package com.app.kotlin

import android.app.Activity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.SharingHelper
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle

class LayoutUtils {


    companion object {
        val getInstance: LayoutUtils by lazy {
            LayoutUtils()
        }
    }

    fun generateShareSheetDialog(
        context: Activity, deeplinkData: Any,
        branchListener: Branch.BranchLinkShareListener
    ) {
        val linkProperties = LinkProperties().apply {
            channel = "g_drive"
            campaign = "task"
            feature = "marketing"
            addControlParameter("\$android_url", context.resources.getString(R.string.branch_host))
            addControlParameter(
                "\$deeplink_path", Gson().toJson(deeplinkData)
            )
        }
        val shareSheetStyle =
            ShareSheetStyle(context, "Check this out!", "This stuff is awesome: ")
                .setCopyUrlStyle(
                    ContextCompat.getDrawable(context, android.R.drawable.ic_menu_send),
                    "Copy",
                    "Added to clipboard"
                )
                .setMoreOptionStyle(
                    ContextCompat.getDrawable(context, android.R.drawable.ic_menu_search),
                    "Show more"
                )
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With")

        BranchUniversalObject().apply {
            canonicalIdentifier = UtilsClass.getRandomId()
            title = context.resources.getString(R.string.app_name)
            setContentDescription(context.resources.getString(R.string.share_description))
            showShareSheet(
                context,
                linkProperties,
                shareSheetStyle, branchListener
            )
        }
    }


}