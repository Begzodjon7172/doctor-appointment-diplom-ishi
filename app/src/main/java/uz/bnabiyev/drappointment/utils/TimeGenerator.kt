package uz.bnabiyev.drappointment.utils

object TimeGenerator {
    private lateinit var timeList: ArrayList<String>
    fun generateTimes(): ArrayList<String> {
        timeList = ArrayList()
        timeList.add("09:00")
        timeList.add("09:30")
        timeList.add("10:00")
        timeList.add("10:30")
        timeList.add("11:00")
        timeList.add("11:30")
        timeList.add("12:00")
        timeList.add("13:00")
        timeList.add("13:30")
        timeList.add("14:00")
        timeList.add("14:30")
        timeList.add("15:00")
        timeList.add("15:30")
        timeList.add("16:00")
        timeList.add("16:30")

        return timeList
    }

}