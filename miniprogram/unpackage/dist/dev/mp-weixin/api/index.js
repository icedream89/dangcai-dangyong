"use strict";
const mockEnterpriseCategories = [
  { id: 1, name: "全部", icon: "🏢", count: 12 },
  { id: 2, name: "电子信息", icon: "💻", count: 4 },
  { id: 3, name: "装备制造", icon: "⚙️", count: 3 },
  { id: 4, name: "现代农业", icon: "🌾", count: 2 },
  { id: 5, name: "食品加工", icon: "🍜", count: 2 },
  { id: 6, name: "化工建材", icon: "🏗️", count: 1 }
];
const mockEnterprises = [
  {
    id: 1,
    enterpriseName: "当阳市科技有限公司",
    unifiedCode: "91420500MA4XXXXX01",
    legalPerson: "张三",
    industry: "电子信息",
    scale: "中型",
    address: "当阳市玉阳路XX号",
    intro: "专注于软件开发和信息技术服务的高新技术企业，拥有自主知识产权20余项",
    logo: "https://images.unsplash.com/photo-1560179707-f14e90ef3623?w=200",
    images: [
      "https://images.unsplash.com/photo-1560179707-f14e90ef3623?w=400",
      "https://images.unsplash.com/photo-1551434678-e076c223a692?w=400"
    ],
    tags: ["高新技术企业", "创新企业"],
    isRecommended: true
  },
  {
    id: 2,
    enterpriseName: "当阳市装备制造有限公司",
    unifiedCode: "91420500MA4XXXXX02",
    legalPerson: "李四",
    industry: "装备制造",
    scale: "大型",
    address: "当阳市开发区XX路",
    intro: "专业从事智能装备研发制造的企业，产品远销国内外",
    logo: "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=200",
    images: [
      "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=400"
    ],
    tags: ["制造业", "智能装备"],
    isRecommended: true
  },
  {
    id: 3,
    enterpriseName: "当阳市农业发展科技有限公司",
    unifiedCode: "91420500MA4XXXXX03",
    legalPerson: "王五",
    industry: "现代农业",
    scale: "中型",
    address: "当阳市XX镇",
    intro: "致力于现代农业技术推广应用，助力乡村振兴",
    logo: "https://images.unsplash.com/photo-1625246333195-98d50ef849c1?w=200",
    images: [
      "https://images.unsplash.com/photo-1625246333195-98d50ef849c1?w=400"
    ],
    tags: ["农业", "乡村振兴"],
    isRecommended: false
  },
  {
    id: 4,
    enterpriseName: "当阳市食品有限公司",
    unifiedCode: "91420500MA4XXXXX04",
    legalPerson: "赵六",
    industry: "食品加工",
    scale: "中型",
    address: "当阳市工业园区",
    intro: "专业食品加工企业，生产绿色健康的食品产品",
    logo: "https://images.unsplash.com/photo-1560179707-f14e90ef3623?w=200",
    images: [
      "https://images.unsplash.com/photo-1560179707-f14e90ef3623?w=400"
    ],
    tags: ["食品企业", "绿色食品"],
    isRecommended: true
  }
];
const mockPolicies = [
  {
    id: 1,
    policyTitle: "当阳市科技创新专项资金管理办法",
    policyNo: "当科文〔2024〕1号",
    policyType: "市级",
    category: "科技创新",
    issueDate: "2024-01-10",
    issueDept: "当阳市科技局",
    summary: "为鼓励企业科技创新，设立专项资金，对符合条件的科技项目给予资金支持",
    content: "详细政策内容...",
    isTop: true,
    isRecommended: true
  },
  {
    id: 2,
    policyTitle: "湖北省中小企业发展促进条例",
    policyNo: "鄂政发〔2023〕XX号",
    policyType: "省级",
    category: "企业发展",
    issueDate: "2023-12-01",
    issueDept: "湖北省人民政府",
    summary: "为促进中小企业健康发展，优化营商环境，提供一系列扶持措施",
    content: "详细政策内容...",
    isTop: false,
    isRecommended: true
  },
  {
    id: 3,
    policyTitle: "关于促进制造业高质量发展的若干措施",
    policyNo: "国办发〔2023〕XX号",
    policyType: "国家级",
    category: "制造业",
    issueDate: "2023-11-20",
    issueDept: "国务院办公厅",
    summary: "为推动制造业高端化、智能化、绿色化发展，出台以下支持措施",
    content: "详细政策内容...",
    isTop: true,
    isRecommended: true
  }
];
const mockCourses = [
  {
    id: 1,
    title: "高新技术企业申报培训",
    category: "政策解读",
    courseType: "文档",
    coverImage: "https://images.unsplash.com/photo-1517048676732-d65bc937f952?w=400",
    summary: "详细讲解高新技术企业申报流程、条件、材料准备等注意事项",
    author: "市科技局",
    viewCount: 2345,
    isRecommended: true
  },
  {
    id: 2,
    title: "企业税收优惠政策详解",
    category: "财务税务",
    courseType: "视频",
    coverImage: "https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=400",
    summary: "系统介绍各项税收优惠政策，帮助企业合理享受税收减免",
    author: "市税务局",
    viewCount: 1876,
    isRecommended: true
  },
  {
    id: 3,
    title: "企业数字化转型案例分享",
    category: "数字化",
    courseType: "视频",
    coverImage: "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=400",
    summary: "分享本地企业数字化转型成功案例，提供实践参考",
    author: "张老师",
    viewCount: 3456,
    isRecommended: true
  }
];
const mockProducts = [
  {
    id: 1,
    enterpriseId: 1,
    enterpriseName: "当阳市科技有限公司",
    productName: "智能办公管理系统",
    category: "软件",
    coverImage: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400",
    price: 5e4,
    unit: "套",
    description: "为企业提供全方位的智能化办公解决方案",
    salesCount: 25
  },
  {
    id: 2,
    enterpriseId: 2,
    enterpriseName: "当阳市装备制造有限公司",
    productName: "数控加工中心",
    category: "装备制造",
    coverImage: "https://images.unsplash.com/photo-1567789884554-0b844b597180?w=400",
    price: 68e4,
    unit: "台",
    description: "高精度数控加工设备，适用于精密零部件加工",
    salesCount: 8
  },
  {
    id: 3,
    enterpriseId: 3,
    enterpriseName: "当阳市农业发展科技有限公司",
    productName: "有机大米",
    category: "农产品",
    coverImage: "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400",
    price: 15,
    unit: "斤",
    description: "生态种植，绿色有机，口感香甜",
    salesCount: 156
  }
];
exports.mockCourses = mockCourses;
exports.mockEnterpriseCategories = mockEnterpriseCategories;
exports.mockEnterprises = mockEnterprises;
exports.mockPolicies = mockPolicies;
exports.mockProducts = mockProducts;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/index.js.map
