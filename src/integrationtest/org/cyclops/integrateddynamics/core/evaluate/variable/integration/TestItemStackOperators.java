package org.cyclops.integrateddynamics.core.evaluate.variable.integration;

import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.http.util.Asserts;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.block.BlockCreativeEnergyBattery;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.variable.*;
import org.cyclops.integrateddynamics.core.test.IntegrationBefore;
import org.cyclops.integrateddynamics.core.test.IntegrationTest;
import org.cyclops.integrateddynamics.core.test.TestHelpers;
import org.cyclops.integrateddynamics.item.ItemWrench;

/**
 * Test the different logical operators.
 * @author rubensworks
 */
public class TestItemStackOperators {

    private static final DummyValueType DUMMY_TYPE = DummyValueType.TYPE;
    private static final DummyVariable<DummyValueType.DummyValue> DUMMY_VARIABLE =
            new DummyVariable<DummyValueType.DummyValue>(DUMMY_TYPE, DummyValueType.DummyValue.of());

    private DummyVariableItemStack iApple;
    private DummyVariableItemStack iApple2;
    private DummyVariableItemStack iBeef;
    private DummyVariableItemStack iEnderPearl;
    private DummyVariableItemStack iHoe;
    private DummyVariableItemStack iHoe100;
    private DummyVariableItemStack iHoeEnchanted;
    private DummyVariableItemStack iPickaxe;
    private DummyVariableItemStack iStone;
    private DummyVariableItemStack iBucketLava;
    private DummyVariableItemStack iWrench;
    private DummyVariableItemStack iEnergyBatteryEmpty;
    private DummyVariableItemStack iEnergyBatteryFull;
    private DummyVariableItemStack iIronOre;

    private DummyVariableBlock bStone;
    private DummyVariableBlock bObsidian;

    private DummyVariable<ValueTypeString.ValueString> sStickWood;
    private DummyVariable<ValueTypeString.ValueString> sPlankWood;

    private DummyVariable<ValueTypeInteger.ValueInteger> int100;
    private DummyVariable<ValueTypeInteger.ValueInteger> int200;

    @IntegrationBefore
    public void before() {
        iApple = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.APPLE)));
        iApple2 = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.APPLE, 2)));
        iBeef = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.BED)));
        iEnderPearl = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.ENDER_PEARL)));
        iHoe = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND_HOE)));
        iHoe100 = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND_HOE, 1, 100)));
        ItemStack hoeEnchanted = new ItemStack(Items.DIAMOND_HOE);
        EnchantmentHelpers.setEnchantmentLevel(hoeEnchanted, Enchantments.AQUA_AFFINITY, 1);
        hoeEnchanted.setRepairCost(10);
        iHoeEnchanted = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(hoeEnchanted));
        iPickaxe = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND_PICKAXE)));
        iStone = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Blocks.STONE)));
        iBucketLava = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.LAVA_BUCKET)));
        iWrench = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(ItemWrench.getInstance())));
        iEnergyBatteryEmpty = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(BlockCreativeEnergyBattery.getInstance())));
        ItemStack energyBatteryFull = new ItemStack(BlockCreativeEnergyBattery.getInstance());
        IEnergyStorage energyStorage = energyBatteryFull.getCapability(CapabilityEnergy.ENERGY, null);
        energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false);
        iEnergyBatteryFull = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(energyBatteryFull));
        iIronOre = new DummyVariableItemStack(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Blocks.IRON_ORE)));

        bStone = new DummyVariableBlock(ValueObjectTypeBlock.ValueBlock.of(Blocks.STONE.getDefaultState()));
        bObsidian = new DummyVariableBlock(ValueObjectTypeBlock.ValueBlock.of(Blocks.OBSIDIAN.getDefaultState()));

        sStickWood = new DummyVariable<>(ValueTypes.STRING, ValueTypeString.ValueString.of("stickWood"));
        sPlankWood = new DummyVariable<>(ValueTypes.STRING, ValueTypeString.ValueString.of("plankWood"));

        int100 = new DummyVariable<>(ValueTypes.INTEGER, ValueTypeInteger.ValueInteger.of(100));
        int200 = new DummyVariable<>(ValueTypes.INTEGER, ValueTypeInteger.ValueInteger.of(200));
    }

    /**
     * ----------------------------------- SIZE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackSize() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 1, "size(apple:1) = 1");

        IValue res2 = Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple2});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 2, "size(apple:2) = 2");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeSizeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeSizeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeSize() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_SIZE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- MAXSIZE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackMaxSize() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 64, "maxsize(apple) = 64");

        IValue res2 = Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iEnderPearl});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 16, "maxsize(enderpearl) = 16");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxSizeMaxSizeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxSizeMaxSizeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeMaxSize() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXSIZE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISSTACKABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsStackable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), true, "isstackable(apple) = true");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "isstackable(hoe) = false");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsStackableIsStackableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsStackableIsStackableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsStackable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISSTACKABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISDAMAGEABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsDamageable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isdamageable(apple) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isdamageable(hoe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsDamageableIsDamageableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsDamageableIsDamageableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsDamageable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISDAMAGEABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- DAMAGE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackDamage() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "damage(hoe:0) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iHoe100});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 100, "damage(hoe:100) = 100");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputDamageDamageLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputDamageDamageSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeDamage() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_DAMAGE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- MAXDAMAGE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackMaxDamage() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "maxdamage(apple) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 1561, "maxdamage(hoe) = 1561");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxDamageMaxDamageLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputMaxDamageMaxDamageSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeMaxDamage() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MAXDAMAGE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISENCHANTED -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsEnchanted() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isenchanted(hoe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isenchanted(hoeenchanted) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantedIsEnchantedLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantedIsEnchantedSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsEnchanted() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTED.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISENCHANTABLE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsEnchantable() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isenchantable(apple) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iHoe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isenchantable(hoe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantableIsEnchantableLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsEnchantableIsEnchantableSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsEnchantable() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISENCHANTABLE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- REPAIRCOST -----------------------------------
     */

    @IntegrationTest
    public void testItemStackRepairCost() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "repaircost(apple) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 10, "repaircost(hoe:10) = 10");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRepairCostRepairCostLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRepairCostRepairCostSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeRepairCost() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_REPAIRCOST.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- RARITY -----------------------------------
     */

    @IntegrationTest
    public void testItemStackRarity() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeString.ValueString, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res1).getRawValue(), EnumRarity.COMMON.rarityName, "rarity(apple) = common");

        IValue res2 = Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res2).getRawValue(), EnumRarity.RARE.rarityName, "rarity(hoeenchanted) = rare");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRarityRarityLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputRarityRaritySmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeRarity() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_RARITY.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- STRENGTH_VS_BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackStrengthVsBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iHoe, bStone});
        Asserts.check(res1 instanceof ValueTypeDouble.ValueDouble, "result is a double");
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res1).getRawValue(), 1.0D, "strengthvsblock(hoe, stone) = 1.0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iPickaxe, bStone});
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res2).getRawValue(), 8.0D, "strengthvsblock(pickaxe, stone) = 8.0");

        IValue res3 = Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iPickaxe, bObsidian});
        TestHelpers.assertEqual(((ValueTypeDouble.ValueDouble) res3).getRawValue(), 8.0D, "strengthvsblock(pickaxe, obsidian) = 8.0");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputStrengthVsBlockStrengthVsBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputStrengthVsBlockStrengthVsBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeStrengthVsBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STRENGTH_VS_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- CAN_HARVEST_BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackCanHarvestBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iHoe, bStone});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "canharvestblock(hoe, stone) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iPickaxe, bStone});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "canharvestblock(pickaxe, stone) = true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iPickaxe, bObsidian});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "canharvestblock(pickaxe, obsidian) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanHarvestBlockCanHarvestBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanHarvestBlockCanHarvestBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeCanHarvestBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CAN_HARVEST_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- BLOCK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackBlock() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{iStone});
        Asserts.check(res1 instanceof ValueObjectTypeBlock.ValueBlock, "result is a block");
        TestHelpers.assertEqual(((ValueObjectTypeBlock.ValueBlock) res1).getRawValue().get(), Blocks.STONE.getDefaultState(), "block(stone) = stone");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputBlockBlockLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputBlockBlockSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeBlock() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_BLOCK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISFLUIDSTACK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsFluidStack() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isfluidstack(hoe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isfluidstack(bucketlava) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsFluidStackIsFluidStackLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsFluidStackIsFluidStackSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsFluidStack() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFLUIDSTACK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FLUIDSTACK -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFluidStack() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueObjectTypeFluidStack.ValueFluidStack, "result is a fluidstack");
        TestHelpers.assertEqual(((ValueObjectTypeFluidStack.ValueFluidStack) res1).getRawValue().isPresent(), false, "fluidstack(hoe) = null");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueObjectTypeFluidStack.ValueFluidStack) res2).getRawValue().get().isFluidStackIdentical(new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME)), true, "fluidstack(bucketlava) = lava:1000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackFluidStackLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackFluidStackSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFluidStack() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACK.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FLUIDSTACK_CAPACITY -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFluidStackCapacity() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is a fluidstack");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "fluidstackcapacity(hoe) = 0");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iBucketLava});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), Fluid.BUCKET_VOLUME, "fluidstackcapacity(bucketlava) = 1000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackCapacityFluidStackCapacityLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFluidStackCapacityFluidStackCapacitySmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFluidStackCapacity() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FLUIDSTACKCAPACITY.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISNBTEQUAL -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsNBTEqual() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iHoe, iPickaxe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isnbtequal(hoe, pickaxe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iHoe, iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "isnbtequal(hoe, hoeenchanted) = false");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iPickaxe, iPickaxe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "isnbtequal(pickaxe, pickaxe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsNBTEqualIsNBTEqualLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsNBTEqualIsNBTEqualSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsNBTEqual() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISNBTEQUAL.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISITEMEQUALNONBT -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsItemEqualNoNBT() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iPickaxe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "israwitemequal(hoe, pickaxe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "israwitemequal(hoe, hoeenchanted) = true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iPickaxe, iPickaxe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "israwitemequal(pickaxe, pickaxe) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsItemEqualNoNBTLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsItemEqualNoNBTSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsItemEqualNoNBT() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISRAWITEMEQUAL -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsRawItemEqual() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iPickaxe});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "israwitemequal(hoe, pickaxe) = false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iHoeEnchanted});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "israwitemequal(hoe, hoeenchanted) = true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iPickaxe, iPickaxe});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "israwitemequal(pickaxe, pickaxe) = true");

        IValue res4 = Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iHoe, iHoe100});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res4).getRawValue(), true, "israwitemequal(hoe, hoe:100) = true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsRawItemEqualIsRawItemEqualLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple, iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputIsRawItemEqualIsRawItemEqualSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsRawItemEqual() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISRAWITEMEQUAL.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- MODNAME -----------------------------------
     */

    @IntegrationTest
    public void testItemStackModName() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_MODNAME.evaluate(new IVariable[]{iHoe});
        Asserts.check(res1 instanceof ValueTypeString.ValueString, "result is a string");
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res1).getRawValue(), "Minecraft", "modname(hoe) = Minecraft");

        IValue res2 = Operators.OBJECT_ITEMSTACK_MODNAME.evaluate(new IVariable[]{iWrench});
        TestHelpers.assertEqual(((ValueTypeString.ValueString) res2).getRawValue(), "Integrated Dynamics", "modname(wrench) = Integrated Dynamics");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeModNameLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MODNAME.evaluate(new IVariable[]{iHoe, iHoe});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeModNameSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MODNAME.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeModName() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_MODNAME.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FUELBURNTIME -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFuelBurnTime() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FUELBURNTIME.evaluate(new IVariable[]{iBucketLava});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 20000, "fuelburntime(bucketlava) = 20000");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FUELBURNTIME.evaluate(new IVariable[]{iApple});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 0, "fuelburntime(apple) = 0");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFuelBurnTimeFuelBurnTimeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FUELBURNTIME.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputFuelBurnTimeFuelBurnTimeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FUELBURNTIME.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFuelBurnTime() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FUELBURNTIME.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- CANBURN -----------------------------------
     */

    @IntegrationTest
    public void testItemStackCanBurn() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_CANBURN.evaluate(new IVariable[]{iBucketLava});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), true, "canburn(bucketlava) = true");

        IValue res2 = Operators.OBJECT_ITEMSTACK_CANBURN.evaluate(new IVariable[]{iApple});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "canburn(apple) = false");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanBurnCanBurnLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANBURN.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanBurnCanBurnSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANBURN.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeCanBurn() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANBURN.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- CANSMELT -----------------------------------
     */

    @IntegrationTest
    public void testItemStackCanSmelt() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_CANSMELT.evaluate(new IVariable[]{iIronOre});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), true, "cansmelt(ironore) = true");

        IValue res2 = Operators.OBJECT_ITEMSTACK_CANSMELT.evaluate(new IVariable[]{iApple});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), false, "cansmelt(apple) = false");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanSmeltCanSmeltLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANSMELT.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputCanSmeltCanSmeltSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANSMELT.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeCanSmelt() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_CANSMELT.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- OREDICT -----------------------------------
     */

    @IntegrationTest
    public void testItemStackOreDict() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_OREDICT.evaluate(new IVariable[]{iStone});
        Asserts.check(res1 instanceof ValueTypeList.ValueList, "result is a list");
        TestHelpers.assertEqual(((ValueTypeList.ValueList) res1).getRawValue().getLength(), 1, "size(oredict(stone)) = 1");

        IValue res2 = Operators.OBJECT_ITEMSTACK_OREDICT.evaluate(new IVariable[]{iWrench});
        TestHelpers.assertEqual(((ValueTypeList.ValueList) res2).getRawValue().getLength(), 0, "size(oredict(wrench)) = 0");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeOreDictLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT.evaluate(new IVariable[]{iHoe, iHoe});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeOreDictSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeOreDict() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- OREDICT_STACKS -----------------------------------
     */

    @IntegrationTest
    public void testItemStackOreDictStacks() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_OREDICT_STACKS.evaluate(new IVariable[]{sStickWood});
        Asserts.check(res1 instanceof ValueTypeList.ValueList, "result is a list");
        TestHelpers.assertEqual(((ValueTypeList.ValueList) res1).getRawValue().getLength(), OreDictionary.getOres("stickWood").size(), "size(oredict_stacks(stickWood))");

        IValue res2 = Operators.OBJECT_ITEMSTACK_OREDICT_STACKS.evaluate(new IVariable[]{sPlankWood});
        TestHelpers.assertEqual(((ValueTypeList.ValueList) res2).getRawValue().getLength(), OreDictionary.getOres("plankWood").size(), "size(oredict_stacks(plankWood))");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeOreDictStacksLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT_STACKS.evaluate(new IVariable[]{sStickWood, sStickWood});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeOreDictStacksSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT_STACKS.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeOreDictStacks() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_OREDICT_STACKS.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- WITHSIZE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackWithSize() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_WITHSIZE.evaluate(new IVariable[]{iApple, int100});
        Asserts.check(res1 instanceof ValueObjectTypeItemStack.ValueItemStack, "result is an itemstack");
        TestHelpers.assertEqual(((ValueObjectTypeItemStack.ValueItemStack) res1).getRawValue().getCount(), 100, "withsize(apple, 100).stacksize == 100");

        IValue res2 = Operators.OBJECT_ITEMSTACK_WITHSIZE.evaluate(new IVariable[]{iBeef, int200});
        TestHelpers.assertEqual(((ValueObjectTypeItemStack.ValueItemStack) res2).getRawValue().getCount(), 200, "withsize(beef, 200).stacksize == 200");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeWithSizeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_WITHSIZE.evaluate(new IVariable[]{iApple, int100, int100});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeWithSizeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_WITHSIZE.evaluate(new IVariable[]{iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeWithSize() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_WITHSIZE.evaluate(new IVariable[]{DUMMY_VARIABLE, DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- ISFECONTAINER -----------------------------------
     */

    @IntegrationTest
    public void testItemStackIsFeContainer() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeBoolean.ValueBoolean, "result is a boolean");
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), false, "isfecontainer(apple) == false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{iEnergyBatteryEmpty});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res2).getRawValue(), true, "isfecontainer(energyBatteryEmpty) == true");

        IValue res3 = Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{iEnergyBatteryFull});
        TestHelpers.assertEqual(((ValueTypeBoolean.ValueBoolean) res3).getRawValue(), true, "isfecontainer(energyBatteryFull) == true");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeIsFeContainerLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{iApple, iApple});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeIsFeContainerSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeIsFeContainer() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_ISFECONTAINER.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- STOREDFE -----------------------------------
     */

    @IntegrationTest
    public void testItemStackStoredFe() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "storedfe(apple) == false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{iEnergyBatteryEmpty});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 0, "storedfe(energyBatteryEmpty) == 0");

        IValue res3 = Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{iEnergyBatteryFull});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res3).getRawValue(), 100000, "storedfe(energyBatteryFull) == 100000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeStoredFeLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{iEnergyBatteryEmpty, iEnergyBatteryEmpty});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeStoredFeSmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeStoredFe() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_STOREDFE.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

    /**
     * ----------------------------------- FECAPACITY -----------------------------------
     */

    @IntegrationTest
    public void testItemStackFeCapacity() throws EvaluationException {
        IValue res1 = Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{iApple});
        Asserts.check(res1 instanceof ValueTypeInteger.ValueInteger, "result is an integer");
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res1).getRawValue(), 0, "fecapacity(apple) == false");

        IValue res2 = Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{iEnergyBatteryEmpty});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res2).getRawValue(), 100000, "fecapacity(energyBatteryEmpty) == 100000");

        IValue res3 = Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{iEnergyBatteryFull});
        TestHelpers.assertEqual(((ValueTypeInteger.ValueInteger) res3).getRawValue(), 100000, "fecapacity(energyBatteryFull) == 100000");
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeFeCapacityLarge() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{iEnergyBatteryEmpty, iEnergyBatteryEmpty});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputSizeFeCapacitySmall() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{});
    }

    @IntegrationTest(expected = EvaluationException.class)
    public void testInvalidInputTypeFeCapacity() throws EvaluationException {
        Operators.OBJECT_ITEMSTACK_FECAPACITY.evaluate(new IVariable[]{DUMMY_VARIABLE});
    }

}
