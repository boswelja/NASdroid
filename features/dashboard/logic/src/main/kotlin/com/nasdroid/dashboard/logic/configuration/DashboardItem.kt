package com.nasdroid.dashboard.logic.configuration

/**
 * Describes an item to be shown on the dashboard.
 */
sealed interface DashboardItem {
    /**
     * The unique ID used to reference the item.
     */
    val id: Long

    /**
     * Describes a CPU-related dashboard item.
     *
     * @property id The unique ID used to reference the item.
     * @property subtype The [Subtype] of the item. Controls what exactly should be displayed.
     */
    data class Cpu(
        override val id: Long,
        val subtype: Subtype
    ): DashboardItem {
        /**
         * Encapsulates all possible data sources for CPU data.
         */
        enum class Subtype {
            /**
             * Displays the CPU model.
             */
            Model,

            /**
             * Displays recent CPU usage as a graph.
             */
            RecentUsage,

            /**
             * Displays per-core temperatures in a graph.
             */
            TemperaturePerCore,

            /**
             * Displays per-core usage in a graph.
             */
            UsagePerCore,

            /**
             * Displays an overview of all other CPU information.
             */
            Overview,

            /**
             * Displays instantaneous CPU usage as a percentage.
             */
            Usage
        }
    }

    /**
     * Displays a breakdown of instantaneous memory usage.
     */
    data class Memory(override val id: Long): DashboardItem

    /**
     * Describes a Storage-related dashboard item.
     *
     * @property id The unique ID used to reference the item.
     * @property subtype The [Subtype] of the item. Controls what exactly should be displayed.
     */
    data class Storage(
        override val id: Long,
        val subtype: Subtype
    ): DashboardItem {
        /**
         * Encapsulates all possible data sources for Storage data.
         */
        sealed interface Subtype {
            /**
             * Displays an overview of a single pool.
             */
            data class Pool(val pool: String): Subtype

            /**
             * Displays the storage used by a pool as a percentage.
             */
            data class PoolUsage(val pool: String): Subtype

            /**
             * Displays an overview of storage pools on the system.
             */
            data object Storage : Subtype

            /**
             * Displays the number of disks in a given pool that have errors.
             */
            data class DisksWithErrors(val pool: String): Subtype

            /**
             * Displays the number of errors identified in the last scan of a pool.
             */
            data class LastScanErrors(val pool: String): Subtype

            /**
             * Displays the overall status of a pool.
             */
            data class PoolStatus(val pool: String): Subtype
        }
    }

    /**
     * Displays backup tasks and their status.
     */
    data class BackupTasks(override val id: Long): DashboardItem

    /**
     * Describes a Network-related dashboard item to be displayed.
     *
     * @property id The unique ID used to reference the item.
     * @property subtype The [Subtype] of the item. Controls what exactly should be displayed.
     * @property interface The network interface for which data is being requested.
     */
    data class Network(
        override val id: Long,
        val subtype: Subtype,
        val `interface`: String
    ): DashboardItem {
        /**
         * Encapsulates all possible data types for Network-related dashboard items.
         */
        enum class Subtype {
            /**
             * Displays the state of the network interface, as well as traffic information.
             */
            Interface,

            /**
             * Displays the IPV4 address assigned to an interface.
             */
            Ipv4Address,

            /**
             * Displays the IPV6 address assigned to an interface.
             */
            Ipv6Address
        }
    }

    /**
     * Describes a System Information-related dashboard item to be displayed.
     *
     * @property id The unique ID used to reference the item.
     * @property subtype The [Subtype] of the item. Controls what exactly should be displayed.
     */
    data class SystemInformation(
        override val id: Long,
        val subtype: Subtype
    ): DashboardItem {

        /**
         * Encapsulates all possible data types for System Information-related dashboard items.
         */
        enum class Subtype {
            /**
             * Displays the systems host name.
             */
            HostName,

            /**
             * Displays the systems OS version.
             */
            OsVersion,

            /**
             * Displays the systems hardware serial.
             */
            Serial,

            /**
             * Displays the image/logo representing the system.
             */
            SystemImage,

            /**
             * Displays an overview of system information.
             */
            SystemInformation,

            /**
             * Displays the system uptime.
             */
            Uptime
        }
    }
}