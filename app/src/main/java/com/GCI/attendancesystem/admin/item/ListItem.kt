package com.GCI.attendancesystem.admin.item

data class ListItem(val id: Long? = null,
                    val name: String? = null) {
    override fun toString(): String {
        return name.toString()
    }
}