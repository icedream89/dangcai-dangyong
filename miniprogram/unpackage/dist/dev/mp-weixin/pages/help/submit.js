"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      formData: {
        title: "",
        category: "",
        urgency: 1,
        content: "",
        phone: ""
      },
      urgencyOptions: [
        { label: "一般", value: 1, icon: "😊" },
        { label: "紧急", value: 2, icon: "😰" },
        { label: "非常紧急", value: 3, icon: "🚨" }
      ],
      showCategoryPicker: false,
      categories: ["政策咨询", "用电问题", "人才服务", "融资服务", "其他"],
      submitting: false
    };
  },
  methods: {
    selectCategory(category) {
      this.formData.category = category;
      this.showCategoryPicker = false;
    },
    async handleSubmit() {
      if (!this.formData.title) {
        return common_vendor.index.showToast({ title: "请输入问题标题", icon: "none" });
      }
      if (!this.formData.category) {
        return common_vendor.index.showToast({ title: "请选择问题类型", icon: "none" });
      }
      if (!this.formData.content) {
        return common_vendor.index.showToast({ title: "请输入问题描述", icon: "none" });
      }
      this.submitting = true;
      setTimeout(() => {
        this.submitting = false;
        common_vendor.index.showToast({
          title: "提交成功",
          icon: "success"
        });
        setTimeout(() => {
          common_vendor.index.navigateTo({
            url: "/pages/help/my-list"
          });
        }, 1500);
      }, 1e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.formData.title,
    b: common_vendor.o(($event) => $data.formData.title = $event.detail.value),
    c: common_vendor.t($data.formData.category || "请选择问题类型"),
    d: !$data.formData.category ? 1 : "",
    e: common_vendor.o(($event) => $data.showCategoryPicker = true),
    f: common_vendor.f($data.urgencyOptions, (item, index, i0) => {
      return {
        a: common_vendor.t(item.icon),
        b: common_vendor.t(item.label),
        c: index,
        d: $data.formData.urgency === item.value ? 1 : "",
        e: common_vendor.o(($event) => $data.formData.urgency = item.value, index)
      };
    }),
    g: $data.formData.content,
    h: common_vendor.o(($event) => $data.formData.content = $event.detail.value),
    i: common_vendor.t($data.formData.content.length),
    j: $data.formData.phone,
    k: common_vendor.o(($event) => $data.formData.phone = $event.detail.value),
    l: common_vendor.t($data.submitting ? "提交中..." : "提交求助"),
    m: common_vendor.o((...args) => $options.handleSubmit && $options.handleSubmit(...args)),
    n: $data.submitting
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-75e0a299"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/help/submit.js.map
