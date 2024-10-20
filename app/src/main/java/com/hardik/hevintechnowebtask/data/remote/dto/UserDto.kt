package com.hardik.hevintechnowebtask.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.hardik.hevintechnowebtask.domain.model.UserModel

data class UserDto( @SerializedName("limit") val limit: Int = 0,
                    @SerializedName("skip") val skip: Int = 0,
                    @SerializedName("total") val total: Int = 0,
                    @SerializedName("users") val users: List<User> = listOf()
) {
    data class User(
        @SerializedName("address") val address: Address = Address(),
        @SerializedName("age") val age: Int = 0,
        @SerializedName("bank") val bank: Bank = Bank(),
        @SerializedName("birthDate") val birthDate: String = "",
        @SerializedName("bloodGroup") val bloodGroup: String = "",
        @SerializedName("company") val company: Company = Company(),
        @SerializedName("crypto") val crypto: Crypto = Crypto(),
        @SerializedName("ein") val ein: String = "",
        @SerializedName("email") val email: String = "",
        @SerializedName("eyeColor") val eyeColor: String = "",
        @SerializedName("firstName") val firstName: String = "",
        @SerializedName("gender") val gender: String = "",
        @SerializedName("hair") val hair: Hair = Hair(),
        @SerializedName("height") val height: Double = 0.0,
        @SerializedName("id") val id: Int = 0,
        @SerializedName("image") val image: String = "",
        @SerializedName("ip") val ip: String = "",
        @SerializedName("lastName") val lastName: String = "",
        @SerializedName("macAddress") val macAddress: String = "",
        @SerializedName("maidenName") val maidenName: String = "",
        @SerializedName("password") val password: String = "",
        @SerializedName("phone") val phone: String = "",
        @SerializedName("role") val role: String = "",
        @SerializedName("ssn") val ssn: String = "",
        @SerializedName("university") val university: String = "",
        @SerializedName("userAgent") val userAgent: String = "",
        @SerializedName("username") val username: String = "",
        @SerializedName("weight") val weight: Double = 0.0
    ) {
        data class Address(
            @SerializedName("address") val address: String = "",
            @SerializedName("city") val city: String = "",
            @SerializedName("coordinates") val coordinates: Coordinates = Coordinates(),
            @SerializedName("country") val country: String = "",
            @SerializedName("postalCode") val postalCode: String = "",
            @SerializedName("state") val state: String = "",
            @SerializedName("stateCode") val stateCode: String = ""
        ) {
            data class Coordinates(
                @SerializedName("lat") val lat: Double = 0.0,
                @SerializedName("lng") val lng: Double = 0.0
            )
        }

        data class Bank(
            @SerializedName("cardExpire") val cardExpire: String = "",
            @SerializedName("cardNumber") val cardNumber: String = "",
            @SerializedName("cardType") val cardType: String = "",
            @SerializedName("currency") val currency: String = "",
            @SerializedName("iban") val iban: String = ""
        )

        data class Company(
            @SerializedName("address") val address: Address = Address(),
            @SerializedName("department") val department: String = "",
            @SerializedName("name") val name: String = "",
            @SerializedName("title") val title: String = ""
        ) {
            data class Address(
                @SerializedName("address") val address: String = "",
                @SerializedName("city") val city: String = "",
                @SerializedName("coordinates") val coordinates: Coordinates = Coordinates(),
                @SerializedName("country") val country: String = "",
                @SerializedName("postalCode") val postalCode: String = "",
                @SerializedName("state") val state: String = "",
                @SerializedName("stateCode") val stateCode: String = ""
            ) {
                data class Coordinates(
                    @SerializedName("lat") val lat: Double = 0.0,
                    @SerializedName("lng") val lng: Double = 0.0
                )
            }
        }

        data class Crypto(
            @SerializedName("coin") val coin: String = "",
            @SerializedName("network") val network: String = "",
            @SerializedName("wallet") val wallet: String = ""
        )

        data class Hair(
            @SerializedName("color") val color: String = "",
            @SerializedName("type") val type: String = ""
        )
    }
}


fun UserDto.User.toUserModel(): UserModel {
    return UserModel(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        image = this.image
    )
}
