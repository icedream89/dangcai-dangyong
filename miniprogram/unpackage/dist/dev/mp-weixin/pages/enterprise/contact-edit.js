"use strict";
const common_vendor = require("../../common/vendor.js");
const store_user = require("../../store/user.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      formData: {
        enterpriseName: "",
        contactPerson: "",
        contactPhone: "",
        email: "",
        address: "",
        businessHours: ""
      },
      submitting: false
    };
  },
  onLoad() {
    this.loadEnterpriseInfo();
  },
  methods: {
    loadEnterpriseInfo() {
      const enterpriseId = store_user.userStore.getEnterpriseId();
      const enterprise = api_index.mockEnterprises.find((e) => e.id === enterpriseId);
      if (enterprise) {
        this.formData = {
          enterpriseName: enterprise.enterpriseName,
          contactPerson: enterprise.legalPerson || "",
          contactPhone: "",
          email: "",
          address: enterprise.address || "",
          businessHours: "周一至周五 9:00-18:00"
        };
      }
    },
    async handleSubmit() {
      if (!this.formData.contactPerson) {
        return common_vendor.index.showToast({ title: "请输入联系人", icon: "none" });
      }
      if (!this.formData.contactPhone) {
        return common_vendor.index.showToast({ title: "请输入联系电话", icon: "none" });
      }
      this.submitting = true;
      setTimeout(() => {
        this.submitting = false;
        common_vendor.index.showToast({
          title: "保存成功",
          icon: "success"
        });
        setTimeout(() => {
          common_vendor.index.navigateBack();
        }, 1500);
      }, 1e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.formData.enterpriseName,
    b: common_vendor.o(($event) => $data.formData.enterpriseName = $event.detail.value),
    c: $data.formData.contactPerson,
    d: common_vendor.o(($event) => $data.formData.contactPerson = $event.detail.value),
    e: $data.formData.contactPhone,
    f: common_vendor.o(($event) => $data.formData.contactPhone = $event.detail.value),
    g: $data.formData.email,
    h: common_vendor.o(($event) => $data.formData.email = $event.detail.value),
    i: $data.formData.address,
    j: common_vendor.o(($event) => $data.formData.address = $event.detail.value),
    k: $data.formData.businessHours,
    l: common_vendor.o(($event) => $data.formData.businessHours = $event.detail.value),
    m: common_vendor.t($data.submitting ? "保存中..." : "保存联系方式"),
    n: common_vendor.o((...args) => $options.handleSubmit && $options.handleSubmit(...args)),
    o: $data.submitting
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-57e51227"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/enterprise/contact-edit.js.map
