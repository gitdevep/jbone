package cn.jbone.cms.portal.service;

import cn.jbone.cms.api.CategoryApi;
import cn.jbone.cms.api.PluginApi;
import cn.jbone.cms.api.SettingsApi;
import cn.jbone.cms.common.constant.DictionaryConstant;
import cn.jbone.cms.common.dataobject.CategoryDO;
import cn.jbone.cms.common.dataobject.PluginDO;
import cn.jbone.cms.common.dataobject.search.CategorySearchDO;
import cn.jbone.cms.common.dataobject.SettingsDO;
import cn.jbone.cms.common.enums.BooleanEnum;
import cn.jbone.cms.common.enums.StatusEnum;
import cn.jbone.common.rpc.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private SettingsApi settingsApi;

    @Autowired
    private CategoryApi categoryApi;

    @Autowired
    private PluginApi pluginApi;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private LinkService linkService;

    public void setCommonProperties(ModelMap modelMap){
        setSettings(modelMap);
        setMenus(modelMap);
        setGlobalPlugins(modelMap);
    }

    public void setCommonModuleDatas(ModelMap modelMap){
        modelMap.addAttribute("hotArticles",articleService.findHotArticles());
        modelMap.addAttribute("tagCloud",tagService.findTagCloud());
        modelMap.addAttribute("links",linkService.findAll());
    }


    private void setMenus(ModelMap modelMap){
        CategorySearchDO categorySearchDO = new CategorySearchDO();
        categorySearchDO.setInMenu(BooleanEnum.TRUE);
        categorySearchDO.setStatus(StatusEnum.PUBLISH);
        Result<List<CategoryDO>> result = categoryApi.requestCategorysTree(categorySearchDO);
        if(result.isSuccess()){
            modelMap.addAttribute("menus",result.getData());
        }
    }

    private void setSettings(ModelMap modelMap){
        Result<Map<String, String>> settingMap =  settingsApi.getMap();
        if(settingMap.isSuccess()){
            if(!CollectionUtils.isEmpty(settingMap.getData())){
                modelMap.addAttribute("settings",settingMap.getData());
            }
        }
    }

    private void setGlobalPlugins(ModelMap modelMap){
        Result<List<PluginDO>> pluginDOSResult = pluginApi.findByType(DictionaryConstant.ITEM_PLUGIN_TYPE_GLOBAL);
        if(pluginDOSResult.isSuccess()){
            modelMap.addAttribute("globalPlugins",pluginDOSResult.getData());
        }
    }

    public void setAriclePlugins(ModelMap modelMap){
        Result<List<PluginDO>> pluginDOSResult = pluginApi.findByType(DictionaryConstant.ITEM_PLUGIN_TYPE_ARTICLE);
        if(pluginDOSResult.isSuccess()){
            modelMap.addAttribute("articlePlugins",pluginDOSResult.getData());
        }
    }

}
