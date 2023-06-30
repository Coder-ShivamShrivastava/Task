package com.task.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.adapter.AbstractModel
import com.task.adapter.DummyModel
import com.task.adapter.RecyclerAdapter
import kotlinx.parcelize.RawValue

/* Api Response
{
    "facilities": [
    {
        "facility_id": "1",
        "name": "Property Type",
        "options": [
        {
            "name": "Apartment",
            "icon": "apartment",
            "id": "1"
        },
        {
            "name": "Condo",
            "icon": "condo",
            "id": "2"
        },
        {
            "name": "Boat House",
            "icon": "boat",
            "id": "3"
        },
        {
            "name": "Land",
            "icon": "land",
            "id": "4"
        }
        ]
    },
    {
        "facility_id": "2",
        "name": "Number of Rooms",
        "options": [
        {
            "name": "1 to 3 Rooms",
            "icon": "rooms",
            "id": "6"
        },
        {
            "name": "No Rooms",
            "icon": "no-room",
            "id": "7"
        }
        ]
    },
    {
        "facility_id": "3",
        "name": "Other facilities",
        "options": [
        {
            "name": "Swimming Pool",
            "icon": "swimming",
            "id": "10"
        },
        {
            "name": "Garden Area",
            "icon": "garden",
            "id": "11"
        },
        {
            "name": "Garage",
            "icon": "garage",
            "id": "12"
        }
        ]
    }
    ],
    "exclusions": [
    [
        {
            "facility_id": "1",
            "options_id": "4"
        },
        {
            "facility_id": "2",
            "options_id": "6"
        }
    ],
    [
        {
            "facility_id": "1",
            "options_id": "3"
        },
        {
            "facility_id": "3",
            "options_id": "12"
        }
    ],
    [
        {
            "facility_id": "2",
            "options_id": "7"
        },
        {
            "facility_id": "3",
            "options_id": "12"
        }
    ]
    ]
}
*/
@Entity(tableName = "ResponseData")
@Parcelize
data class ResponseModel(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int=0,
    @SerializedName("exclusions")
    var exclusions: List<List<Exclusion?>>? = null,
    @SerializedName("facilities")
    var facilities: List<Facility>? = null
) : Parcelable {
    @Parcelize
    data class Exclusion(
        @SerializedName("facility_id")
        var facilityId: String? = null,
        @SerializedName("options_id")
        var optionsId: String? = null
    ) : Parcelable

    @Parcelize
    data class Facility(
        @SerializedName("facility_id")
        var facilityId: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("options")
        var options: List<Option>? = null,
        var childAdapter: @RawValue RecyclerAdapter<Option>? = null
    ) : Parcelable,AbstractModel() {
        @Parcelize
        data class Option(
            @SerializedName("icon")
            var icon: String? = null,
            @SerializedName("id")
            var id: String? = null,
            @SerializedName("name")
            var name: String? = null,
            var isSelected:Boolean=false,
            var upPairMessage:String = "",
            var hash:HashMap<String, MutableSet<String>>? = null
        ) : Parcelable,AbstractModel()
    }
}