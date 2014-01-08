package tconstruct.preloader;

/*
 * TConstructLoaderContainer is derived from code graciously provided by AlgorithmX2 of Applied Energistics.
 * The code was adapted for this loader by Sunstrike. Any queries should be directed to him, not AlgorithmX2.
 */

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.event.ForgeSubscribe;
import tconstruct.preloader.helpers.PropertyManager;
import tconstruct.preloader.helpers.PropertyManager.PropAccessException;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class TConstructLoaderContainer extends DummyModContainer implements IFMLLoadingPlugin
{

    private final String[] asmTransformers = new String[] { "tconstruct.preloader.AccessTransformers", "tconstruct.preloader.ASMInterfaceRepair" };
    private final ModMetadata md = new ModMetadata();

    public static Logger logger = Logger.getLogger("TCon-Preloader");

    public TConstructLoaderContainer()
    {
        logger.setParent(FMLLog.getLogger());
        logger.info("Scalpel. Suction. Lumber axe. CLEAR! *zap*");
        logger.info("Constructing preloader (Modules: " + Arrays.toString(asmTransformers) + ")");
        try
        {
            PropertyManager.getOrCreateProps();
        }
        catch (PropAccessException ex)
        {
            logger.severe("Could not get or create properties file! Assuming defaults.");
            logger.severe(Arrays.toString(ex.getStackTrace()));
        }

        if (PropertyManager.asmInterfaceRepair_verboseLog)
        {
            logger.warning("ASM Interface Repair is set to be verbose, this is NOT GOOD FOR SERVERS, as it will spam the logs!");
            logger.warning("The debug mode is for debug use only. Turn it off unless instructed otherwise by a TCon developer!");
            logger.warning("To reset advanced settings to default, delete the '" + PropertyManager.propFileName + "' file from the config folder.");
        }

        md.autogenerated = true;
        md.credits = "AlgorithmX2 (original) and Sunstrike (porting)";
        md.modId = getModId();
        md.version = getVersion();
        md.name = getName();
        md.authorList = Arrays.asList("AlgorithmX2", "Sunstrike");
    }

    @ForgeSubscribe
    public void preInit (FMLPreInitializationEvent evt)
    {

    }

    @ForgeSubscribe
    public void init (FMLInitializationEvent evt)
    {

    }

    @ForgeSubscribe
    public void postInit (FMLPostInitializationEvent evt)
    {

    }

    @Override
    public String getModId ()
    {
        return "TConstruct-Preloader";
    }

    @Override
    public String getName ()
    {
        return "Tinkers Corestruct";
    }

    @Override
    public String getVersion ()
    {
        return "0.0.1";
    }

    @Override
    public String getDisplayVersion ()
    {
        return getVersion();
    }

    @Override
    public ModMetadata getMetadata ()
    {
        return md;
    }

    @Override
    public boolean registerBus (EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    //@Override  // Broken in Gradle builds; should work fine in non-gradle.
    public String[] getLibraryRequestClass ()
    {
        return null;
    }

    @Override
    public String[] getASMTransformerClass ()
    {
        return asmTransformers;
    }

    @Override
    public String getModContainerClass ()
    {
        return "tconstruct.preloader.TConstructLoaderContainer";
    }

    @Override
    public String getSetupClass ()
    {
        return null;
    }

    @Override
    public void injectData (Map<String, Object> data)
    {
    }
}
