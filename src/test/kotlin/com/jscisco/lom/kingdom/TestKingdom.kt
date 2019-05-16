package com.jscisco.lom.kingdom

import com.jscisco.lom.actor.Player
import com.jscisco.lom.kingdom.buildings.Bank
import com.jscisco.lom.kingdom.buildings.Inn
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestKingdom {

    val testKingdom: Kingdom = Kingdom("Test Kingdom")

    @Test
    fun `When I build a building, it should be built if it doesn't exist & I have enough gold`() {
        val initialGold: Int = 1000
        testKingdom.addGold(initialGold)
        val inn: Inn = Inn()
        testKingdom.buildBuilding(inn)
        Assertions.assertThat(testKingdom.currentGold).isEqualTo(initialGold - inn.cost)
        Assertions.assertThat(testKingdom.buildings.size).isEqualTo(1)
        Assertions.assertThat(testKingdom.buildings[0]).isEqualTo(inn)
    }

    @Test
    fun `When I build a building, it should not be built if I don't have enough gold`() {
        testKingdom.currentGold = 10
        val inn: Inn = Inn()
        testKingdom.buildBuilding(inn)
        Assertions.assertThat(testKingdom.currentGold).isEqualTo(10)
        Assertions.assertThat(testKingdom.buildings.size).isEqualTo(0)
    }

    @Test
    fun `When I build a building, it should not be built if I have already built it`() {
        testKingdom.buildings.add(Inn())
        testKingdom.currentGold = 1000
        testKingdom.buildBuilding(Inn())
        Assertions.assertThat(testKingdom.currentGold).isEqualTo(1000)
        Assertions.assertThat(testKingdom.buildings.size).isEqualTo(1)

    }

    @Test
    fun `The maximum gold amount should be calculated correctly if I do not have a bank`() {
        Assertions.assertThat(testKingdom.maximumGold).isEqualTo(testKingdom.initialMaximumGold)
    }

    @Test
    fun `The maximum gold amount should be calculated correctly if I do have a bank`() {
        val bank: Bank = Bank()
        testKingdom.buildings.add(bank)
        Assertions.assertThat(testKingdom.maximumGold).isEqualTo(testKingdom.initialMaximumGold + bank.maximumGoldBonus)
    }

    @Test
    fun `The maximum number of heroes should be calculated correctly if I do not have an Inn`() {
        Assertions.assertThat(testKingdom.maximumHeroes).isEqualTo(testKingdom.initialMaximumHeroes)
    }

    @Test
    fun `The maximum number of heroes should be calculated correctly if I do have an Inn`() {
        val inn: Inn = Inn()
        testKingdom.buildings.add(inn)
        Assertions.assertThat(testKingdom.maximumHeroes).isEqualTo(testKingdom.initialMaximumHeroes + inn.maxHeroes)
    }

    @Test
    fun `I should only be able to hire a hero if I have enough money and there is room`() {
        val hero: Player = Player()
        val cost: Int = 123
        val initialGold: Int = 500
        testKingdom.currentGold = initialGold
        testKingdom.hireHero(hero, cost)
        Assertions.assertThat(testKingdom.availableHeroes.size).isEqualTo(1)
        Assertions.assertThat(testKingdom.currentGold).isEqualTo(initialGold - cost)

    }

}